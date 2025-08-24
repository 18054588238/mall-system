package com.personal.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.common.utils.PageUtils;
import com.personal.mall.ware.entity.PurchaseEntity;
import com.personal.mall.ware.entity.vo.MergeVO;
import com.personal.mall.ware.entity.vo.PurchaseVO;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:22:11
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    Integer merge(MergeVO vo);

    void receive(List<Long> purchaseIds);

    void done(PurchaseVO vo);
}

