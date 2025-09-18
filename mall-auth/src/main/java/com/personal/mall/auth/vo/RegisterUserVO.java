package com.personal.mall.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @ClassName RegisterUserVO
 * @Author liupanpan
 * @Date 2025/9/18
 * @Description 注册用户的vo对象
 */
@Data
public class RegisterUserVO {
    @NotBlank
    @Length(min = 4,max = 20,message = "用户名必须是4~20位")
    private String username;
    @NotBlank
    @Length(min = 4,max = 20,message = "密码必须是4~20位")
    private String password;
    @NotBlank
    @Pattern(regexp = "^1[3-9][0-9]{9}$",message = "请输入正确的手机号")
    private String phone;
    @NotBlank
    @Email(message = "请输入正确的邮箱")
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String code;
}
