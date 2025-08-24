package com.personal.mall.coupon.service.impl;

import com.personal.common.dto.SkuReductionDTO;
import com.personal.mall.coupon.entity.MemberPriceEntity;
import com.personal.mall.coupon.entity.SkuLadderEntity;
import com.personal.mall.coupon.service.MemberPriceService;
import com.personal.mall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.coupon.dao.SkuFullReductionDao;
import com.personal.mall.coupon.entity.SkuFullReductionEntity;
import com.personal.mall.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private MemberPriceService memberPriceService;
    @Autowired
    private SkuLadderService ladderService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSkuReduction(SkuReductionDTO dto) {
        SkuFullReductionEntity fullReduction = new SkuFullReductionEntity();
        BeanUtils.copyProperties(dto,fullReduction);
        if (fullReduction.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
            this.save(fullReduction);
        }

        SkuLadderEntity ladderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(dto,ladderEntity);
        ladderEntity.setAddOther(dto.getCountStatus());
        if (ladderEntity.getFullCount() > 0) {
            ladderService.save(ladderEntity);
        }

        if (dto.getMemberPrice() != null && !dto.getMemberPrice().isEmpty()) {
            List<MemberPriceEntity> priceEntityList = dto.getMemberPrice().stream().map(m -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(dto.getSkuId());
                memberPriceEntity.setMemberLevelName(m.getName());
                memberPriceEntity.setMemberPrice(m.getPrice());
                memberPriceEntity.setMemberLevelId(m.getId());
                memberPriceEntity.setAddOther(1);
                return memberPriceEntity;
            }).collect(Collectors.toList());
            memberPriceService.saveBatch(priceEntityList);
        }
    }

}