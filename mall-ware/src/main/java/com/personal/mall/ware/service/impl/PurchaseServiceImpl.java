package com.personal.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.personal.common.constant.WareConstant;
import com.personal.mall.ware.entity.PurchaseDetailEntity;
import com.personal.mall.ware.entity.vo.MergeVO;
import com.personal.mall.ware.entity.vo.PurchaseVO;
import com.personal.mall.ware.service.PurchaseDetailService;
import com.personal.mall.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.ware.dao.PurchaseDao;
import com.personal.mall.ware.entity.PurchaseEntity;
import com.personal.mall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService detailService;
    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0)
                .or().eq("status", 1);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public Integer merge(MergeVO vo) {
        Long purchaseId = vo.getPurchaseId();
        if (purchaseId == null) {
            // 创建采购单
            PurchaseEntity purchase = new PurchaseEntity();
            purchase.setStatus(WareConstant.PurchaseStatus.CREATED.getCode());
            purchase.setCreateTime(new Date());
            purchase.setUpdateTime(new Date());
            this.save(purchase);
            purchaseId = purchase.getId();
        }
        // 更新采购需求状态
        List<Long> items = vo.getItems();
        List<PurchaseDetailEntity> detailEntities = new ArrayList<>();
        for (Long i : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(i);
            detailEntity.setPurchaseId(purchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatus.ASSIGNED.getCode());
            detailEntities.add(detailEntity);
        }
        detailService.updateBatchById(detailEntities);

        // 更新采购单时间
        PurchaseEntity purchaseEntity = this.getById(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
        return 0;
    }

    @Transactional
    @Override
    public void receive(List<Long> purchaseIds) {
        // 更新采购单状态
        List<PurchaseEntity> purchaseList = new ArrayList<>();
        purchaseIds.forEach(purchaseId -> {
            PurchaseEntity purchaseEntity = this.getById(purchaseId);
            if (purchaseEntity.getStatus() == WareConstant.PurchaseStatus.CREATED.getCode() || purchaseEntity.getStatus() == WareConstant.PurchaseStatus.ASSIGNED.getCode()) {
                purchaseEntity.setStatus(WareConstant.PurchaseStatus.RECEIVE.getCode());
                purchaseEntity.setUpdateTime(new Date());
                purchaseList.add(purchaseEntity);
            }
        });
        this.updateBatchById(purchaseList);
        // 更新采购需求状态
        detailService.update(new UpdateWrapper<PurchaseDetailEntity>()
                .set("status", WareConstant.PurchaseDetailStatus.BUYING.getCode())
                .in("purchase_id", purchaseIds));
    }

    @Transactional
    @Override
    public void done(PurchaseVO vo) {
        // 采购需求状态是否完成标志
        AtomicBoolean flag = new AtomicBoolean(true);

        List<PurchaseDetailEntity> detailEntities = vo.getItems().stream().map(i -> {
            PurchaseDetailEntity detailEntity = detailService.getById(i.getId());
            detailEntity.setStatus(WareConstant.PurchaseDetailStatus.FINISH.getCode());
            if (i.getStatus() == WareConstant.PurchaseDetailStatus.HASERROR.getCode()) {
                flag.set(false);
                detailEntity.setStatus(i.getStatus());
            }
            return detailEntity;
        }).collect(Collectors.toList());

        detailService.updateBatchById(detailEntities);

        // 更新采购单状态/时间
        PurchaseEntity purchaseEntity = this.getById(vo.getPurchaseId());
        purchaseEntity.setUpdateTime(new Date());
        purchaseEntity.setStatus(WareConstant.PurchaseStatus.FINISH.getCode());
        if (!flag.get()) {
            purchaseEntity.setStatus(WareConstant.PurchaseStatus.HASERROR.getCode());
        }
        this.updateById(purchaseEntity);

        // 入库
        detailEntities.forEach(detailEntity -> {
            if (detailEntity.getStatus() == WareConstant.PurchaseDetailStatus.FINISH.getCode()) {
                // 采购需求成功的入库
                wareSkuService.addStock(detailEntity);
            }
        });
    }

}