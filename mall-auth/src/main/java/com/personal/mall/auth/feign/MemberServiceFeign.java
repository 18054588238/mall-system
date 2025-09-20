package com.personal.mall.auth.feign;

import com.personal.common.utils.R;
import com.personal.mall.auth.vo.LoginUserVO;
import com.personal.mall.auth.vo.RegisterUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("mall-member")
public interface MemberServiceFeign {

    @PostMapping("/member/member/register")
    public R register(@RequestBody RegisterUserVO vo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody LoginUserVO vo);

}
