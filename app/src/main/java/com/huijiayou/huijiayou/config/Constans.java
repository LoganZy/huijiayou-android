package com.huijiayou.huijiayou.config;

/**
 * Created by lugg on 2017/2/24.
 */

public class Constans {


    public final static boolean DEBUG = true;
    //微信相关
    public static String WXBaseUrl = "https://api.weixin.qq.com/";
    public static String WX_APP_ID = "wxc77febc13d61d07b";
    public static String AppSecret = "4c7582669eb3db8ec0bf8a2f22163397";
    public static String NICKNAME = "nickname";
    public static String HEADIMGURL= "headimgurl";
    public static String ACCESSTOKEN = "accesstoken";
    public static String OPENID = "openid";
    public static String UNIONID ="unionid";
    // public static final String APP_ID = "wxd930ea5d5a258f4f";

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }
    //请求的类型
    public final static String JSONOBJECT = "jsonObject";
    public final static String JSONOARRAY= "jsonArray";


    public final static String URL_wyh = "http://wyh.oil.user.passport.com";//王远航
    public final static String URL_zxg = "http://oilproduct.dev.wanglibao.com";//张孝国

//    public final static String URL_wyh = "http://test.1huangjin.cn/passport";//王远航
//    public final static String URL_zxg = "http://test.1huangjin.cn/pro";//张孝国

    public final static String ACCOUNT = "/service.php?c=account";
    public final static String OILCARD = "/index.php?c=oilcard";
    public final static String PRODUCT = "/index.php?c=product";
    public final static String ORDER = "/index.php?c=order";


    public final static String URL_MESSAGE = "http://wyh.oil.message.com/message.php?c=msg";
    public final static String message_lst = "lst";
    public final static String message_markAll = "markAll";
    public final static String message_checkNewMsg = "checkNewMsg";


    //method
    public final static String MESSAGEAUTH = "messageAuth"; //获取手机验证码
    public final static String SIGNIN = "signin"; //登录
    public final static String SIGNOUT = "signout"; //撤销登录
    public final static String LOGINSTATUS = "loginStatus"; //获取登录状态
    public final static String WEIXIN_AUTH_POST = "weixin_auth_post"; //微信登录
   //weixin_auth_post

    public final static String productList = "productList"; //产品列表
    public final static String getOilCardList = "getOilCardList"; //油卡列表
    public final static String getCity = "getCity"; //可充油城市的列表
    public final static String bindCard = "bindCard"; //绑卡
    public final static String getOilCardInfo = "getOilCardInfo"; //油卡信息查询接口
    public final static String UserPacketsInfo = "UserPacketsInfo"; //获取用户所有红包
    public final static String UserEnableOil = "UserEnableOil"; //返回该用户的可用油滴
    public final static String UserOildropInfo = "UserOildropInfo"; //返回油滴流水
    public final static String order = "order"; //下单


    public final static String registerAgreement = "/HJY/#/register_agreement"; //注册协议
    public final static String userAgreement = "/HJY/#/user_agreement"; //用户协议






    public static final String CODE = "code";
    public static final String DATA = "data";
    public static final String ERROR_MESSAGE = "message";

    public static final String ID_ZHONGSHIHUA = "1";
    public static final String ID_ZHONGSHIYOU = "2";

    public static int lunHui = 1;
}
