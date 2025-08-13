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
> 13：物流
> 14：会员*/
public enum BizCodeEnum {

    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VALID_EXCEPTION(10001,"参数格式异常");

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
