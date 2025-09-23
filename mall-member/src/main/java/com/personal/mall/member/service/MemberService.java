package com.personal.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.common.utils.PageUtils;
import com.personal.mall.member.entity.MemberEntity;
import com.personal.mall.member.entity.vo.AuthResponseVO;
import com.personal.mall.member.entity.vo.LoginVO;
import com.personal.mall.member.entity.vo.RegisterVO;
import com.personal.mall.member.exception.PhoneExsitExecption;
import com.personal.mall.member.exception.UsernameExsitExecption;

import java.util.Map;

/**
 * 会员
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:14:26
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(RegisterVO vo) throws PhoneExsitExecption, UsernameExsitExecption;

    MemberEntity login(LoginVO vo);

    MemberEntity oauthLogin(AuthResponseVO vo);
}

