package com.personal.mall.member.service.impl;

import com.personal.mall.member.entity.MemberLevelEntity;
import com.personal.mall.member.entity.vo.RegisterVO;
import com.personal.mall.member.service.MemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.member.dao.MemberDao;
import com.personal.mall.member.entity.MemberEntity;
import com.personal.mall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    MemberLevelService levelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(RegisterVO vo) {
        MemberEntity member = new MemberEntity();
        levelService.list(new QueryWrapper<MemberLevelEntity>().eq("",))
        member.setLevelId();
        member.setUsername(vo.getUsername());

        member.setMobile(vo.getPhone());
        member.setEmail(vo.getEmail());
        member.setCreateTime(LocalDate.now());
        // 加密 BcryptPasswordEncoder
        member.setPassword(vo.getPassword());
    }

}