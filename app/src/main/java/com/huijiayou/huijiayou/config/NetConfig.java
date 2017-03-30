package com.huijiayou.huijiayou.config;

/**
 * Created by lugg on 2017/3/26.
 */

public class NetConfig {

    public final static String URL_MESSAGE = "http://wyh.oil.message.com/message.php?c=msg";
    public final static String message_lst = "lst";
    public final static String message_markAll = "markAll";
    public final static String message_checkNewMsg = "checkNewMsg";
    public final static String message_mark = "mark";


    public final static String URL_wyh = "http://wyh.oil.user.passport.com";//王远航
    public final static String URL_zxg = "http://oilproduct.dev.wanglibao.com";//张孝国
//    public final static String URL_wyh = "http://test.1huangjin.cn/passport";//王远航
//    public final static String URL_zxg = "http://test.1huangjin.cn/pro";//张孝国

    public final static String ACCOUNT = "/service.php?c=account";
    public final static String OILCARD = "/index.php?c=oilcard";
    public final static String PRODUCT = "/index.php?c=product";
    public final static String ORDER = "/index.php?c=order";
    public final static String PAY = "/index.php?c=pay";


    //User
    public final static String MESSAGEAUTH = "messageAuth"; //获取手机验证码
    public final static String SIGNIN = "signin"; //登录
    public final static String SIGNOUT = "signout"; //撤销登录
    public final static String LOGINSTATUS = "loginStatus"; //获取登录状态
    public final static String WEIXIN_AUTH_POST = "weixin_auth_post"; //微信登录


    public final static String productList = "productList"; //产品列表
    public final static String getOilCardList = "getOilCardList"; //油卡列表
    public final static String getCity = "getCity"; //可充油城市的列表
    public final static String bindCard = "bindCard"; //绑卡
    public final static String getOilCardInfo = "getOilCardInfo"; //油卡信息查询接口
    public final static String UserPacketsInfo = "UserPacketsInfo"; //获取用户所有红包
    public final static String UserEnableOil = "UserEnableOil"; //返回该用户的可用油滴
    public final static String checkIn = "checkIn"; //签到
    public final static String UserOildropInfo = "UserOildropInfo"; //返回油滴流水
    public final static String order = "order"; //下单
    public final static String getOrderList = "getOrderList";
    public final static String getOrderInfo=  "getOrderInfo";
    public final static String CHECKIN ="checkIn";//q签到的接口
    public final static String checkOrder ="checkOrder";//订单支付前调用的接口
    public final static String payChannel ="payChannel";//支付渠道


    public final static String registerAgreement = "/HJY/#/register_agreement"; //注册协议
    public final static String userAgreement = "/HJY/#/user_agreement"; //用户协议

}
