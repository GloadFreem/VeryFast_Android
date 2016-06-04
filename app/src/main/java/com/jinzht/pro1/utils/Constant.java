package com.jinzht.pro1.utils;

/**
 * 常量
 */
public class Constant {

//    public static final String BASE_URL = "http://www.jinzht.com/jinzht/";

//    public static final String BASE_URL = "http://192.168.5.126:8080/jinzht/";

    public static final String BASE_URL = "http://www.jinzht.com:8080/jinzht/";
    /**
     * 私钥
     */
    public static final String PRIVATE_KEY = "jinzht_server_security";
    /**
     * 照相选择照片
     */
    public static final int TAKE_PHOTO = 0;
    /**
     * 从相册选择照片
     */
    public static final int CHOOSE_PHOTO = 1;
    /**
     * 剪裁图片
     */
    public static final int CUT_PHOTO = 3;
    /**
     * 身份类型——项目方
     */
    public static final int USERTYPE_XMF = 1;
    /**
     * 身份类型——投资人
     */
    public static final int USERTYPE_TZR = 2;
    /**
     * 身份类型——投资机构
     */
    public static final int USERTYPE_TZJG = 3;
    /**
     * 身份类型——智囊团
     */
    public static final int USERTYPE_ZNT = 4;
    /**
     * 登录接口
     */
    public static final String LOGIN = "loginUser.action";
    /**
     * 微信登录接口
     */
    public static final String WECHATLOGIN = "wechatLoginUser.action";
    /**
     * 注册接口
     */
    public static final String REGISTER = "registUser.action";
    /**
     * 获取验证码接口
     */
    public static final String VERIFYCODE = "verifyCode.action";
    /**
     * 客服接口
     */
    public static final String CUSTOMERSERVICE = "customServiceSystem.action";
    /**
     * 重置密码接口
     */
    public static final String RESETPASSWORD = "resetPassWordUser.action";
    /**
     * 选择身份类型接口
     */
    public static final String SETUSERTYPE = "updateIdentiyTypeUser.action";
    /**
     * 获得省份列表接口
     */
    public static final String GETPROVINCELIST = "getProvinceListAuthentic.action";
    /**
     * 根据省份id获取市列表接口
     */
    public static final String GETCITYLIST = "getCityListByProvinceIdAuthentic.action";
    /**
     * 获取投资领域列表接口
     */
    public static final String GETFIELDLIST = "getIndustoryAreaListAuthentic.action";
}
