package com.creditease.checkcars.config;

public class Config {

  public static final String DESKEY = "H05JDz46sV2SZ7l5LBHHC2DO";
  //
  // public static final String URL_REPORT =
  // "http://sfkctest.vpluser.com/sfkc_wx/reportRead?ruuid=";
  //


  // // 测试
  // public static final String ROOT_URL = "http://115.28.158.2:8050/usedcar/";
  //
  // // weixin port
  // // 测试
  // public static final String WeiXin_URL = "http://sfkctest.vpluser.com/WxApi/sendNeedPayModen";
  //
  // // 测试
  // public static final String QRCODE =
  // "http://sfkctest.vpluser.com/wx_api/createQuickAppointmentQr/";
  // // 测试二维码
  // public static final String VIN_QUERY_URL =
  // "http://sfkctest.vpluser.com/publicApi/queryCarInfoByVin/";
  // // 测试 金牌师傅的个人页面
  // public static final String WeiXin_APPRAISER_HOMEPAGE_URL =
  // "http://sfkctest.vpluser.com/index/shifuInfoH5/";
  //
  // // 测试 质保销售页面
  // public static final String ALLCOVER_HOME_URL =
  // "http://sfkctest.vpluser.com/saler/views/list_ass.html?salerId=";

  // 正式
  public static final String ROOT_URL = "http://shifukanche.com:8060/usedcar/";

  // 正式
  public static final String WeiXin_URL = "http://shifukanche.com/WxApi/sendNeedPayModen";

  // 生产
  public static final String QRCODE = "http://shifukanche.com/wx_api/createQuickAppointmentQr/";

  public static final String VIN_QUERY_URL = "http://shifukanche.com/publicApi/queryCarInfoByVin/";
  // 金牌师傅的个人页面
  public static final String WeiXin_APPRAISER_HOMEPAGE_URL =
      "http://shifukanche.com/index/shifuInfoH5/";
  // 质保销售页面
  public static final String ALLCOVER_HOME_URL =
      "http://www.shifukanche.com/saler/views/list_ass.html?salerId=";

  // 师傅课堂
  public static final String URL_APPRAISER_SCHOOL = "http://shifukanche.com/master/classRoom";
  // 活动列表
  public static final String URL_ACTIVITIES = "http://shifukanche.com/activity/activityList";
  // 我要估车网页，由车300提供
  public static final String URL_OLDCAR_SOURCE = "https://www.che300.com/shifu";
  public static final String URL_NATIVE_SOURCE = "file:///android_asset/shifu.html";

  /**
   * 验证码
   */
  public static final String WS_USERS_GET_VERIFY_CODE_URL = ROOT_URL
      + "Appraiser/sendVerificationCode";

  /**
   * 版本检测
   */
  public static final String WS_VERSION_CHECK_URL = ROOT_URL + "queryUpdate";

  /**
   * 注册
   */
  public static final String WS_USERS_USER_REGISTER_URL = ROOT_URL + "userRegister";

  /**
   * 登陆
   */
  public static final String WS_USERS_USER_LOGIN_URL = ROOT_URL + "loginAppraiser";

  /**
   * 登出
   */
  public static final String WS_USERS_USER_LOGOUT_URL = ROOT_URL + "userLoginOut";

  /**
   * 获取车检订单
   */
  public static final String WS_USERS_CARORDERS_GET_URL = ROOT_URL + "queryPageListForOrder";

  /**
   * 添加车检报告
   */
  public static final String WS_USERS_REPORT_ADD_URL = ROOT_URL + "addReport";

  /**
   * 添加车检报告
   */
  public static final String WS_USERS_REPORT_GET_URL = ROOT_URL + "queryReportByuuid";

  /**
   * 提交车检报告
   */
  public static final String WS_USERS_REPORT_UPDATE_URL = ROOT_URL + "updateReport";

  /**
   * 提交车检报告
   */
  public static final String WS_USERS_REPORT_UPDATE_IMAGE_URL = ROOT_URL
      + "updateReportAttrForImage";

  /**
   * 提交检测项图片
   */
  public static final String WS_USERS_REPORT_UPLOAD_IMAGE_ITEM_URL = Config.ROOT_URL
      + "updateReportCarForImage";

  /**
   * 完成服务
   */
  public static final String WS_USERS_CARORDER_FINISH_URL = ROOT_URL + "updateOrderStatus";

  /**
   * 订单支付详情
   */
  public static final String WS_USERS_CARORDER_PAYINFO_URL = ROOT_URL + "Pay/queryPayOrder";

  /**
   * 完成订单支付
   */
  public static final String WS_USERS_CARORDER_PAY_FINISH_URL = ROOT_URL + "addPay";

  /**
   * 数据字典更新
   */
  public static final String WS_USERS_BASE_REPORT_UPDATE_URL = ROOT_URL + "queryReportUpdate";

  /**
   * 师傅基本资料上传
   */
  public static final String WS_MINE_DATA_SAVE_URL = ROOT_URL + "addAppraiser";
  /**
   * 获取对师傅的评价列表
   */
  public static final String WS_MINE_TOPIC_LIST = ROOT_URL + "queryPageListForOrderAppraise";

  /**
   * 上传头像接口
   */
  public static final String WS_MINE_HEAD_PHOTO = ROOT_URL + "updateUserHeadPic";

  /**
   * 获取师傅基本信息
   */
  public static final String WS_MINE_BASIC_DATA = ROOT_URL + "queryAppraiserByuuid";

  /**
   * 更新消息推送的用户信息
   */
  public static final String WS_UPDATE_MSGPUSH_USERDATA = ROOT_URL + "updateAppraiseForPM";

  /**
   * 获取问答信息接口
   */
  public static final String WS_GET_QUESTION_ANSWER_DATA = ROOT_URL
      + "Question/queryPageListForQuestion";
  /**
   * 提交问答信息接口
   */
  public static final String WS_UPDATE_QUESTION_ANSWER_DATA = ROOT_URL
      + "Question/addOrUpdateQuestionAnswer";

  /**
   * 增加or修改问题接口
   */
  public static final String WS_UPDATE_QUESTION_DATA = ROOT_URL + "Question/addOrUpdateQuestion";
  /**
   * 记录关于订单和报告的各种事件的发生时间
   */
  public static final String WS_TIME_RECORDNODE = ROOT_URL + "Time/recordTimeNode";
  /**
   * 记录师傅具体的定位位置
   */
  public static final String WS_GET_LOCATION = ROOT_URL + "addLoginLog";

  /**
   * 获取师傅预检测报告
   */
  public static final String WS_GET_PRE_CARREPORT = ROOT_URL + "Order/queryPreCheckOrder";
  /**
   * 添加师傅预检测报告
   */
  public static final String WS_ADD_PRE_CARREPORT = ROOT_URL + "Order/addPreCheckOrder";

}
