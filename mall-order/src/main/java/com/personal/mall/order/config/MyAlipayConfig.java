package com.personal.mall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.personal.mall.order.vo.PayVO;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "9021000156614832";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCq8FXR+loeVKY3w3P66kMLsmUB6x7kXDFgJeGs2JS0efyB1+kk9vM7GXwfisrupbkG44hgJH67CXa7UhzMc/1Srz6jxUBfTOG10mx9NbDfwnRLtiYG8F0jPBDWCvyj8jM1xzwsNJ6AmUJzFKlUCmGzUB0x/IdCzQnqcgviVKXmw9PLsYD+fyEPj86FtkYd8tekmg3xwvlBoz1r+RoTm/42MiuiKrIZ6tzr1uoWtXr4wLHV10RUAjPhUOTCiueupuipGWYeLLzRQIrzQqAGvc3OiEn7zJd5Xk2Cq1poWzPYVKlVPTipNcCa47AzS1c3OKqLQMoACOSPJB/V3q4E232vAgMBAAECggEAJWxwnYP97t4BdfvojGp1Oz5XNtBO9xfoB3DDs5/HDqwWSCcI9/rIzYuKzS0JuK23kHaICOR0imZMBD6551kNgOi1Ag3RRgwC7lefQNpcWuCrrVe3g5VkDq670OCe5xXDeGaiGdt7SBRFpARF/gw3z5Yeji8HPey92wIU7/wwRhEF0qSPrsQRnLPbltYqvoK+sYaohJRmgo8uBvVa5NE6kgMInO2JixnxJnKc3PPu/4OBE27s2HSQYaqikuZBSmfe5pingyJnbmu0yXqq06rIWSloF2+e94bkPV3h9/rffkvRKqKB7by+ubCn77cbt/Pgq+00rGSx/YhvNLTlmmpqIQKBgQDV9clVxCPfz671mLMP1HdhMkhvWmQzxo/e+ENbO8HG6Y22cZ0JevuQXGguOcc3fHx3Pa5fO0aSnck+RbDrojosYZDxHy92LnhsO7UZhLnrbcfVAJAlJiDumHsrfgnhLdPsSGOz1lRr68aNetJLEI/IIJ2UlwuK0V9bgiVkvuJK+wKBgQDMhpLXc/SeqFYHn9TdkEDrM315kICd0XkhZLvHXmFmRgxAqbMDCKQkqHy8A3Sm6XzQWFsiMwr6JklFCrfLpg+lrs/JyTblarOCLxr5FaYItDjVdP8ZDtXRmZMvBlnA+zu8Ad1c2lODPpOB64TDFgm4hstzL97DHQC0GyBfezbZ3QKBgFI3KxFg2CQlauaGsa/Qhbr8lmbrhft7PkeAgGlrUe+8SS8lAyHkdwwm5gV1QD7q/+zV7846OckXBoB+SRiScuYg65Ar2pIAE1gLrXBL1yFHQvVvTPRpbVghkZrJyMQVVwzBWEZ/glsh817kJjCgAY/2MHcrf0ztXUYNgY7v5obHAoGAc4cUjQUfyTr3FWlwGRMxyGmWV8OLExpO3NEEzNIAHIA2zZPN3UnVRiuyAZXGH75F1X4DRGcmrTRRMOGj/jG4JvN+xdGV47MI4OFw0AAtgD8k11Y1/laF6tZf8DEbI8oF7OX0v01H3RvMwJdjr7rUFNp3M4J3xyirzBCCkcyMqDUCgYBlTz6x4Mjq4S+78/ywbVqa8109rd7dYOu4marMKryhjO1whfbinzi64Q6QmEzPeSMtf5j/I8a/5gC+mrA0GbvVl0Vg+/qK+pDD91wakGx/kP69X9jY1GJBX4fKRRseOQs0iBXhAAf/BncIhGpgUa+vKULhg+pc/cW2OfCXCLD1xQ==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzQf6qOWAyQepc/5WILcx93FujU7IyoFrFkzAv8WaXqW+cidYPOGxoSSCDaS1mcHS+7XfvqKj3UHga98DoFXiPLZn5iDXkXXEmZ2Hxw0H27mJo4+PRj0sE3bZehuN6lZSw2jA9n1xyxIhaV5CjdKemtALkxehOcHDJdydAh8I1AAHmnMbpjo0DJLGE5e0dLkN6n5BUuVCi76IrcobD3YsQbjeJetfJ90cmzGyv0ij/hYmb/Oyb5J+DX7iZPrmopikbPE4Ns8s8hJ6VXAIGcBMzR+gD66pNEROpvwiIoZ2MUnjtQ26yf4fJMMBg5eZXbGnSJ1lEumL8LpqYYEazK69QIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://order.mall.com/orderPay/notifyUrl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://order.mall.com/orderPay/returnUrl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";

    public String pay(PayVO payVO) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(MyAlipayConfig.gatewayUrl,
                MyAlipayConfig.app_id,
                MyAlipayConfig.merchant_private_key,
                "json",
                MyAlipayConfig.charset,
                MyAlipayConfig.alipay_public_key,
                MyAlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(MyAlipayConfig.return_url);// 支付宝服务器异步通知页面
        alipayRequest.setNotifyUrl(MyAlipayConfig.notify_url);// 支付宝服务器同步通知页面

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = payVO.getOut_trade_no();
        //付款金额，必填
        String total_amount = payVO.getTotal_amount();
        //订单名称，必填
        String subject = payVO.getSubject();
        //商品描述，可空
        String body = payVO.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println("支付result--html--------->"+result);
        /*<form name="punchout_form" method="post" action="https://openapi.alipay.com/gateway.do?
        charset=utf-8&
        method=alipay.trade.page.pay&
        sign=kpVC9JN76NzUK3EPKkA3YbAlIb8pIKrT%2FPD1PBk8TThFLis6pbcPYuG%2FnxnM7eJSfm4tdv8KyW5OeibMd6IlQJFnKJ0YcPrI%2BZjgJY%2BC6%2BiM4AvzuI%2BwhInRW%2B7zJwCNyqVZRWmTnKEm4kcNjiQ8R%2BQTWBVwFFATDY%2B4I1%2BvJi%2BHLMp2vVfFANOvscgoR5J1KedIttc%2Fp7GttDc7M6SO7Fmc5sibGxPBFbpadtQDozuTRPY694JyF57P2hqDy3grMuy%2BK0oz1Fj3l6PrLVVNB7INfA%2FsAIjdPIuPeC%2BGZtb%2FxRulC3NM4w8STCF3yQsVU7V3pzw7P8iIeBbs4BBXJQ%3D%3D&
        return_url=http%3A%2F%2Forder.mall.com%2ForderPay%2FreturnUrl&
        notify_url=http%3A%2F%2Forder.mall.com%2Fpayed%2FnotifyUrl&
        version=1.0&
        app_id=9021000156614832&
        sign_type=RSA2&
        timestamp=2025-10-07+11%3A52%3A55&
        alipay_sdk=alipay-sdk-java-dynamicVersionNo&
        format=json">
        <input type="hidden" name="biz_content" value="{&quot;out_trade_no&quot;:&quot;202510071151543441975408717985751041&quot;,&quot;total_amount&quot;:&quot;6999.00&quot;,&quot;subject&quot;:&quot;123456q&quot;,&quot;body&quot;:&quot;null&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}">
        <input type="submit" value="立即支付" style="display:none" >
        </form>
        */
        return result;
    }

//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
//    public static void logResult(String sWord) {
//        FileWriter writer = null;
//        try {
//            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
//            writer.write(sWord);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (writer != null) {
//                try {
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
