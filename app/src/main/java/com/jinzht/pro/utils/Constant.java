package com.jinzht.pro.utils;

/**
 * 常量
 */
public class Constant {

    public static final String BASE_URL = "http://192.168.5.146:8080/jinzht/";
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
     * 微信验证手机号码
     */
    public static final String WECHATREGISTER = "completeUserInfo.action";
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
     * 获取投资人列表
     */
    public static final String GETINVESTORLIST = "requestInvestorList.action";
    /**
     * 获取投资人详情
     */
    public static final String GETINVESTORDETAIL = "requestInvestorDetail.action";
    /**
     * 提交项目
     */
    public static final String SUBMITPROJECT = "requestProjectCommit.action";
    /**
     * 关注投资人
     */
    public static final String COLLECTINVESTOR = "requestInvestorCollect.action";
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
    /**
     * 获取PPT数据
     */
    public static final String GETPPTDATA = "requestRecorData.action";
    /**
     * 获取路演项目成员
     */
    public static final String GETMEMBER = "requestProjectMember.action";
    /**
     * 路演项目现场交流评论
     */
    public static final String GETCOMMENTSLIST = "requestProjectSceneCommentList.action";
    /**
     * 交流评论
     */
    public static final String SCENECOMMENT = "requestSceneComment.action";
    /**
     * 投资
     */
    public static final String INVEST = "requestInvestProject.action";
    /**
     * 修改公司名
     */
    public static final String CHANGECOMPANY = "requestModifyCompany.action";
    /**
     * 修改职位
     */
    public static final String CHANGEPOSITION = "requestModifyPosition.action";
    /**
     * 修改所在地
     */
    public static final String CHANGEADDR = "requestModifyCity.action";
    /**
     * 获取交易账单列表
     */
    public static final String GETBILLLIST = "requestTradeList.action";
    /**
     * 获取我的关注列表
     */
    public static final String GETMYCOLLECT = "requestMineCollection.action";
    /**
     * 充值
     */
    public static final String RECHARGE = "requestAccountCharge.action";
    /**
     * 提现
     */
    public static final String WITHDRAW = "requestWithDraw.action";
    /**
     * 获取我的活动列表
     */
    public static final String GETMYACTIVITYLIST = "requestMineAction.action";
    /**
     * 查询金条账户
     */
    public static final String GETGOLDACCOUNT = "requestGoldAccount.action";
    /**
     * 获取金条收支明细列表
     */
    public static final String GETGOLDINOUTLIST = "requestGoldTradeList.action";
    /**
     * 邀请好友送金条
     */
    public static final String SHAREAPP = "requestInviteFriends.action";
    /**
     * 金条使用规则
     */
    public static final String GOLDGETRULE = "requestGoldGetRule.action";
    /**
     * 金条使用规则
     */
    public static final String GOLDUSERULE = "requestGoldUseRule.action";
    /**
     * 获取项目中心的项目列表
     */
    public static final String GETPROCENTERLIST = "requestProjectCenter.action";
    /**
     * 项目方查看提交记录
     */
    public static final String GETCOMMITRECORDS = "requestProjectCommitRecords.action";
    /**
     * 忽略项目
     */
    public static final String IGNOREPROJECT = "requestIgorneProjectCommit.action";
    /**
     * 修改密码
     */
    public static final String CHANGEPASSWORD = "requestModifyPassword.action";
    /**
     * 更换手机号码
     */
    public static final String CHANGETEL = "requestChangeBindTelephone.action";
    /**
     * 版本更新
     */
    public static final String UPDATE = "versionInfoSystem.action";
    /**
     * 注销登录
     */
    public static final String LOGOUT = "signupUser.action";
    /**
     * 平台介绍
     */
    public static final String PLATFORMINTRODUCE = "requestPlatformIntroduce.action";
    /**
     * 新手指南
     */
    public static final String NEWUSERGUIDE = "requestNewUseIntroduce.action";
    /**
     * 用户协议
     */
    public static final String USERPROTOCOL = "requestUserProtocol.action";
    /**
     * 免责声明
     */
    public static final String DISCLAIMER = "requestLawerIntroduce.action";
    /**
     * 意见反馈
     */
    public static final String FEEDBACK = "requestFeedBack.action";
    /**
     * 获取站内信列表
     */
    public static final String GETMESSAGELIST = "requestInnerMessageList.action";
    /**
     * 标记站内信为已读
     */
    public static final String SETISREAD = "requestHasReadMessage.action";
    /**
     * 删除所选项
     */
    public static final String DELETECHECKED = "requestDeleteInnerMessage.action";
    /**
     * 是否有未读站内信
     */
    public static final String HAVENOTREADMESSAGE = "requestHasMessageInfo.action";
    /**
     * 获取当日登录金条奖励
     */
    public static final String GETGOLDAWARD = "requestUserGoldGetInfo.action";
    /**
     * 催一催
     */
    public static final String URGE = "requestAuthenticQuick.action";
    /**
     * 上传项目的邮箱和电话
     */
    public static final String GETUPLOADPROJECTINFO = "requestuploadProjectInfo.action";
    /**
     * 活动搜索
     */
    public static final String ACTIVITYSEARCH = "requestSearchAction.action";

    /**
     * ==============================以下为易宝支付接口=======================================
     */

    /**
     * 平台编号platformNo
     */
    public static final String PLATFORMNO = "10013200657";
//    public static final String PLATFORMNO = "10013200660";
    /**
     * 签名接口，type = 0表示网关，type = 1表示直连。
     */
    public static final String SIGN = "signVerify.action";
    /**
     * 易宝网关接口
     */
    public static final String YEEPAY_GATEWAY = "http://220.181.25.233:8081/member/bhawireless/";
//    public static final String YEEPAY_GATEWAY = "https://member.yeepay.com/member/bhawireless/";
    /**
     * 易宝直连接口
     */
    public static final String YEEPAY_DIRECT = "http://220.181.25.233:8081/member/bhaexter/bhaController";
//    public static final String YEEPAY_DIRECT = "https://member.yeepay.com/member/bhaexter/bhaController";
    /**
     * 易宝回跳页面
     */
//    public static final String YEEPAY_CALLBACK = "requestFinanceCallBackUrl.action";
    public static final String YEEPAY_CALLBACK = "YibaoCallback.jsp";
    /**
     * 易宝回调通知
     */
    public static final String YEEPAY_NOTIFY = "requestFinanceNotifyUrl.action";
    /**
     * 易宝账户查询
     */
    public static final String YEEPAY_ACCOUNTINFO = "ACCOUNT_INFO";
    /**
     * 易宝账户注册
     */
    public static final String YEEPAY_REGISTER = "toRegister";
    /**
     * 易宝账户充值
     */
    public static final String YEEPAY_RECHARGE = "toRecharge";
    /**
     * 易宝投标
     */
    public static final String YEEPAY_TENDE = "toCpTransaction";
    /**
     * 易宝绑卡
     */
    public static final String YEEPAY_BIND = "toBindBankCard";
    /**
     * 易宝解绑
     */
    public static final String YEEPAY_UNBIND = "UNBIND_CARD";
    /**
     * 易宝提现
     */
    public static final String YEEPAY_WITHDRAW = "toWithdraw";
}
