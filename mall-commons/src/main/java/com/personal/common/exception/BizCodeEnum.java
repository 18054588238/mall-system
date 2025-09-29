package com.personal.common.exception;

/**
 * @ClassName BizCodeEnum
 * @Author liupanpan
 * @Date 2025/8/13
 * @Description 错误编码和错误信息的枚举类
 */
/*响应编码的规制制订，因为随着后面的业务越来越复杂，我们在响应异常信息的时候尽量准确的给客户端有用的提示信息。

> 通用的错误列表，响应的编码统一为5位数字，前面两位约定为业务场景，最后三位约定为错误码
> 10：表示通用
> /001:参数格式错误 10001
> /002:未知异常 10002
> 11：商品
> 12：订单
> 13：购物车
> 14：物流
> 15：会员*/
public enum BizCodeEnum {

    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VALID_EXCEPTION(10001,"参数格式异常"),
    VALID_MAIL_EXCEPTION(10002,"邮箱发送频率太高，稍等一会发送!"),
    PRODUCT_UP_EXCEPTION(11001,"上架商品保存到es有误"),
    NO_STOCK_EXCEPTION(14001,"商品锁定库存失败"),
    USERNAME_EXSIT_EXCEPTION(15001,"用户名已存在"),
    PHONE_EXSIT_EXCEPTION(15002,"手机号已存在"),
    USERINFO_NOT_EXSIT_EXCEPTION(15003,"账号或密码输入有误，请重新输入");

    private Integer code;
    private String msg;

    BizCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
