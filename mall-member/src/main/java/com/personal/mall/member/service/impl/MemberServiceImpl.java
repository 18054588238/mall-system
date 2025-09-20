package com.personal.mall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.personal.common.utils.HttpUtils;
import com.personal.mall.member.entity.MemberLevelEntity;
import com.personal.mall.member.entity.vo.AuthResponseVO;
import com.personal.mall.member.entity.vo.LoginVO;
import com.personal.mall.member.entity.vo.RegisterVO;
import com.personal.mall.member.exception.PhoneExsitExecption;
import com.personal.mall.member.exception.UsernameExsitExecption;
import com.personal.mall.member.service.MemberLevelService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public MemberEntity login(LoginVO vo) {
        // 用户名/手机号/邮箱和密码的验证
        String userInfo = vo.getUsername();
        String password = vo.getPassword();

        MemberEntity entity = this.getOne(new QueryWrapper<MemberEntity>()
                .eq("username", userInfo)
                .or().eq("mobile", userInfo)
                .or().eq("email", userInfo));
        if (entity != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean matches = encoder.matches(password, entity.getPassword());
            if (matches) {
                // 表示登录成功
                return entity;
            }
        }
        return null;
    }

    // weibo社交平台登录实现
    @Override
    public MemberEntity oauthLogin(AuthResponseVO vo) throws Exception {
        /*两种情况：
        * 1. 第一次社交登录，需要先注册
        * 2. 非首次社交登录，更新社交平台token信息
        * */
        MemberEntity entity = this.getOne(new QueryWrapper<MemberEntity>().eq("uid", vo.getUid()));
        if (entity != null) {
            // 更新
            entity.setAccessToken(vo.getAccessToken());
            entity.setExpiresIn(vo.getExpiresIn());
            this.updateById(entity);
            return entity;
        }
        // 新增
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setSocialUid(vo.getUid());
        memberEntity.setExpiresIn(vo.getExpiresIn());
        memberEntity.setAccessToken(vo.getAccessToken());

        // 获取社交平台的用户信息
        Map<String, String> map = new HashMap<>();
        HttpResponse resUserInfo = HttpUtils.doGet("https://api.weibo.com",
                "/2/users/show.json",
                "get",
                null,
                map
        );
        int statusCode = resUserInfo.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            // 查询失败
        }
        HttpEntity userInfoEntity = resUserInfo.getEntity();
        String string = EntityUtils.toString(userInfoEntity);
        JSONObject jsonObject = JSON.parseObject(string);
        String screenName = jsonObject.getString("screen_name");// 昵称
        String gender = jsonObject.getString("gender");// m：男、f：女、n：未知
        String imageUrl = jsonObject.getString("profile_image_url");// 头像url
        memberEntity.setNickname(screenName);
        memberEntity.setGender(Objects.equals(gender, "m") ? 1:0);
        memberEntity.setHeader(imageUrl);
        return memberEntity;
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