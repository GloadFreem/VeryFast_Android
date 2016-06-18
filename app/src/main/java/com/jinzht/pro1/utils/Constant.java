package com.jinzht.pro1.utils;

/**
 * 常量
 */
public class Constant {

//    public static final String BASE_URL = "http://www.jinzht.com/jinzht/";

    public static final String BASE_URL = "http://192.168.5.129:8080/jinzht/";

//    public static final String BASE_URL = "http://www.jinzht.com:8080/jinzht/";
    /**
     * 微信APP_ID
     */
    public static final String APP_ID = "wx33aa0167f6a81dac";
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
    public static final String GETVERIFYCODE = "verifyCode.action";
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
    /**
     * 认证接口
     */
    public static final String AUTHENTICATE = "requestAuthentic.action";
    /**
     * 获取投资人能力列表
     */
    public static final String GETCAPACITYLIST = "getProtocolAuthentic.action";
    /**
     * 获取圈子列表
     */
    public static final String GETCIRCLELIST = "requestFeelingList.action";
    /**
     * 圈子点赞
     */
    public static final String PRISECIRCLE = "requestPriseFeeling.action";
    /**
     * 圈子详情
     */
    public static final String GETCIRCLEDETAIL = "requestFeelingDetail.action";
    /**
     * 圈子评论
     */
    public static final String COMMENTCIRCLE = "requestCommentFeeling.action";
    /**
     * 发表圈子动态
     */
    public static final String RELEASECIRCLE = "requestPublicFeeling.action";
    /**
     * 分享圈子
     */
    public static final String SHARECIRCLE = "requestShareFeeling.action";
    /**
     * 获取活动列表
     */
    public static final String GETACTIVITYLIST = "requestActionList.action";
    /**
     * 活动报名
     */
    public static final String APPLYACTIVITY = "requestAttendAction.action";
    /**
     * 活动详情
     */
    public static final String GETACTIVITYDETAIL = "requestDetailAction.action";
    /**
     * 获取全部活动报名人
     */
    public static final String GETALLACTIVITYAPPLYS = "requestAttendListAction.action";
    /**
     * 获取全部评论人
     */
    public static final String GETALLACTIVITYCOMMENTS = "requestPriseListAction.action";
    /**
     * 活动点赞
     */
    public static final String PRISEACTIVITY = "requestPriseAction.action";
    /**
     * 活动评论
     */
    public static final String COMMENTACTIVITY = "requestCommentAction.action";
    /**
     * 活动分享
     */
    public static final String SHAREACTIVITY = "requestShareAction.action";
    /**
     * 投资人列表
     */
    public static final String GETINVESOTORLIST = "requestInvestorList.action";
    /**
     * 提交项目
     */
    public static final String SUBMITPROJECT = "requestProjectCommit.action";
    /**
     * 关注投资人
     */
    public static final String COLLECTINVESTOR = "requestInvestorCollect.action";
    /**
     * 项目中心
     */
    public static final String GETPROJECTCENTER = "requestProjectCenter.action";
    /**
     * 分享投资人
     */
    public static final String SHAREINVESTOR = "requestShareInvestor.action";
    /**
     * 检查用户是否已经登录
     */
    public static final String ISLOGIN = "isLoginUser.action";
    /**
     * 用户信息
     */
    public static final String GETUSERINFO = "authenticInfoUser.action";
    /**
     * 获取邀请码
     */
    public static final String GETINVITECODE = "requestInviteCode.action";
    /**
     * 更换头像
     */
    public static final String CHANGEFAVICON = "requestChangeHeaderPicture.action";
    /**
     * 获取项目列表
     */
    public static final String GETPROJECTLIST = "requestProjectList.action";
    /**
     * 获取banner信息
     */
    public static final String GETBANNERINFO = "bannerSystem.action";
    /**
     * 获取项目详情
     */
    public static final String GETPROJECTDETAIL = "requestProjectDetail.action";
    /**
     * 关注项目
     */
    public static final String COLLECTPROJECT = "requestProjectCollect.action";
    /**
     * 分享项目
     */
    public static final String SHAREPROJECT = "requestProjectShare.action";
    /**
     * 项目评论
     */
    public static final String COMMENTPROJECT = "requestProjectComment.action";
    /**
     * 获取项目评论列表
     */
    public static final String GETPROJECTCOMMENTS = "requestProjectCommentList.action";
    /**
     * 获取路演音频
     */
    public static final String GETROADSHOWVOICE = "requestScene.action";
}
