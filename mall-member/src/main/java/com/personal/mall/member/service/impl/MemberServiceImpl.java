package com.personal.mall.member.service.impl;

import com.personal.mall.member.entity.MemberLevelEntity;
import com.personal.mall.member.entity.vo.RegisterVO;
import com.personal.mall.member.exception.PhoneExsitExecption;
import com.personal.mall.member.exception.UsernameExsitExecption;
import com.personal.mall.member.service.MemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        MemberLevelEntity defaultLevel = levelService.getDefaultLevel();
        member.setLevelId(defaultLevel.getId());
        // 账号和手机号的重复性校验
        verifyNameAndPhone(vo.getUsername(),vo.getPhone());
        member.setUsername(vo.getUsername());
        member.setMobile(vo.getPhone());
        member.setEmail(vo.getEmail());
        member.setCreateTime(LocalDate.now());
        // 加密 BcryptPasswordEncoder
        String encode = new BCryptPasswordEncoder().encode(vo.getPassword());
        member.setPassword(encode);
        this.save(member);
    }

    private void verifyNameAndPhone(String username, String phone) {
        long nameCount = this.count(new QueryWrapper<MemberEntity>().eq("username", username));
        long phoneCount = this.count(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (nameCount > 0) {
            throw new UsernameExsitExecption("用户名已存在");
        }
        if (phoneCount > 0) {
            // 抛出异常，响应给前端
            throw new PhoneExsitExecption("手机号已存在");
        }
    }

}