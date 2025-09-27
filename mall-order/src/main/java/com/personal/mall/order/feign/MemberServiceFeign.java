package com.personal.mall.order.feign;

import com.personal.mall.order.vo.MemberAddressVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(value = "mall-member")
public interface MemberServiceFeign {
    @RequestMapping("/member/memberreceiveaddress/{memberId}/address")
    @ResponseBody
    public List<MemberAddressVO> getAddressInfo(@PathVariable("memberId") Long memberId);
}
