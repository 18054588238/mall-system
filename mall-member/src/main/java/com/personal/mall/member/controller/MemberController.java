package com.personal.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.personal.common.exception.BizCodeEnum;
import com.personal.mall.member.entity.vo.RegisterVO;
import com.personal.mall.member.exception.PhoneExsitExecption;
import com.personal.mall.member.exception.UsernameExsitExecption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.mall.member.entity.MemberEntity;
import com.personal.mall.member.service.MemberService;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.R;



/**
 * 会员
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:14:26
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public R register(@RequestBody RegisterVO vo) {

        try {
            memberService.register(vo);
        } catch (PhoneExsitExecption phoneExsitExecption) {
            return R.error(BizCodeEnum.PHONE_EXSIT_EXCEPTION.getCode(), BizCodeEnum.PHONE_EXSIT_EXCEPTION.getMsg());
        } catch (UsernameExsitExecption usernameExsitExecption) {
            return R.error(BizCodeEnum.USERNAME_EXSIT_EXCEPTION.getCode(),  BizCodeEnum.USERNAME_EXSIT_EXCEPTION.getMsg());
        } catch (Exception e) {
            return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(),  BizCodeEnum.UNKNOW_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
