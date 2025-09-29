package com.personal.mall.member.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.mall.member.entity.MemberReceiveAddressEntity;
import com.personal.mall.member.service.MemberReceiveAddressService;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.R;



/**
 * 会员收货地址
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:14:26
 */
@RestController
@RequestMapping("member/memberreceiveaddress")
public class MemberReceiveAddressController {
    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;

    @RequestMapping("/address/{id}")
    public MemberReceiveAddressEntity getByAddressId(@PathVariable("id") Long id){
        return memberReceiveAddressService.getById(id);
    }

    @RequestMapping("/{memberId}/address")
    @ResponseBody
    public List<MemberReceiveAddressEntity> getAddressInfo(@PathVariable("memberId") Long memberId) {
        return memberReceiveAddressService.getAddressInfo(memberId);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberReceiveAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);

        return R.ok().put("memberReceiveAddress", memberReceiveAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.save(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.updateById(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
