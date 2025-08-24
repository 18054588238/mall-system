package com.personal.mall.coupon.service.impl;

import com.personal.common.dto.SpuBoundsDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.coupon.dao.SpuBoundsDao;
import com.personal.mall.coupon.entity.SpuBoundsEntity;
import com.personal.mall.coupon.service.SpuBoundsService;


@Service("spuBoundsService")
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuBoundsEntity> page = this.page(
                new Query<SpuBoundsEntity>().getPage(params),
                new QueryWrapper<SpuBoundsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBounds(SpuBoundsDTO dto) {
        SpuBoundsEntity boundsEntity = new SpuBoundsEntity();
        BeanUtils.copyProperties(dto,boundsEntity);
        this.save(boundsEntity);
    }

}