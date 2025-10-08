package com.personal.mall.coupon.service.impl;

import com.personal.mall.coupon.entity.SeckillSkuRelationEntity;
import com.personal.mall.coupon.service.SeckillSkuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.coupon.dao.SeckillSessionDao;
import com.personal.mall.coupon.entity.SeckillSessionEntity;
import com.personal.mall.coupon.service.SeckillSessionService;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    @Autowired
    private SeckillSkuRelationService seckillSkuRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSessionEntity> getSeckillSkuLatestDays(String[] latestDays) {
        List<SeckillSessionEntity> seckillSessionEntities = this.list(new QueryWrapper<SeckillSessionEntity>()
                .between("start_time", latestDays[0], latestDays[1]));

        List<SeckillSessionEntity> data = seckillSessionEntities.stream().map(seckillSessionEntity -> {
            List<SeckillSkuRelationEntity> relationEntities = seckillSkuRelationService.list(new QueryWrapper<SeckillSkuRelationEntity>()
                    .eq("promotion_session_id", seckillSessionEntity.getId()));
            seckillSessionEntity.setSeckillSkuRelationList(relationEntities);
            return seckillSessionEntity;
        }).collect(Collectors.toList());

        return data;
    }

}