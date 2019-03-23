package com.liuniukeji.mixin.net;

/**
 * 接口调用地址
 */
@SuppressWarnings("all")
public class UrlUtils {

    //public static final String APIHTTP = "http://mixin3.pro2.liuniukeji.net/";
    public static final String APIHTTP = "http://mixin.rltx18.com/";//正式

    public static final String getSchoolList = APIHTTP + "/Api/School/getList";//学校列表
    public static final String getDepartmentList = APIHTTP + "/Api/School/getDepartmentList";//学院列表
    public static final String getGradeList = APIHTTP + "/Api/School/getClassList";//年级列表
    public static final String forgetPassword = APIHTTP + "/Api/Public/forgetPassword";//忘记密码
    public static final String register = APIHTTP + "Api/Public/registerByUsername";//注册
    public static final String login = APIHTTP + "Api/Public/loginByUsername";//登录
    public static final String smsCode = APIHTTP + "/Api/Public/sendSmsCode";//获取短信验证码
    public static final String checkSmsCode = APIHTTP + "/Api/Public/checkSmsCode";//校验短信验证码
    public static final String inviteCode = APIHTTP + "/Api/Member/inviteCode";//我的邀请码
    public static final String qrCodeUrl = APIHTTP + "/Api/Member/getMyCode";//我的二维码

    public static final String feedback = APIHTTP + "/Api/Member/feedBack";//意见反馈
    public static final String signature = APIHTTP + "/Api/Member/edit";//签名修改
    public static final String birthday = APIHTTP + "/Api/Member/edit";//生日修改
    public static final String address = APIHTTP + "/Api/Member/edit";//地址修改
    public static final String hobby = APIHTTP + "/Api/Member/edit";//兴趣爱好修改
    public static final String modifyPhone = APIHTTP + "/Api/Member/modifyPhone";//电话号码修改
    public static final String modifyPassword = APIHTTP + "/Api/Member/modifyPassword";//密码修改
    public static final String authCheck = APIHTTP + "/Api/Member/getUserExpand";//查询实名认证信息
    public static final String submitAuthCheck = APIHTTP + "/Api/Member/updateUserExpand";//提交实名认证信息
    public static final String getVipMonth = APIHTTP + "/Api/Order/getVipList";//获取会员月数信息列表
    public static final String buyVip = APIHTTP + "/Api/Order/pointsBuyVip";//使用学分购买会员月
    public static final String getUserInfo = APIHTTP + "/Api/Member/getUserInfo ";//获取用户信息
    public static final String getScoreInfo = APIHTTP + "/Api/Order/getPointsList";//获取学分列表信息
    public static final String getScoreList = APIHTTP + "/Api/AccountLog/getList";//获取学分变动明细列表信息
    public static final String getBlackList = APIHTTP + "/Api/Member/myBlackList";//黑名单列表
    public static final String getAttentionList = APIHTTP + "/Api/Member/follows";//我的关注列表
    public static final String getFansList = APIHTTP + "/Api/Member/fans";//我的粉丝列表
    public static final String getVisitorList = APIHTTP + "/Api/Member/myVisitor";//我的访客列表
    public static final String getReplyList = APIHTTP + "/Api/Moments/myReply";//我的回复列表
    public static final String getPeopleNearbyList = APIHTTP + "/Api/Member/getNearby";//附近的人列表
    public static final String getGroupNearbyList = APIHTTP + "/Api/Group/getNearby";//附近的群组列表
    public static final String getInterestGroupList = APIHTTP + "/Api/Group/myGroup";//兴趣组列表
    public static final String postDynamics = APIHTTP + "/Api/Moments/addMoments";//发布图文&视频信息
    public static final String getFollowSchool = APIHTTP + "/Api/Moments/followSchool";//获取关注学校列表
    public static final String getFollowSchoolTop = APIHTTP + "/Api/Moments/followSchoolTop";//获取首页顶部导航关注学校列表
    public static final String addOrCancelFocus = APIHTTP + "/Api/Moments/addOrCancelFocus";//(学校)关注和取消关注
    public static final String report = APIHTTP + "/Api/Member/report";//举报
    public static final String getOtherCampus = APIHTTP + "/Api/Moments/getOtherCampus";//其他学校动态列表
    public static final String getRecommend = APIHTTP + "/Api/Moments/getRecommend";//获取推荐动态列表
    public static final String getCampus = APIHTTP + "/Api/Moments/getCampus";//获取校园动态列表
    public static final String getFocusMoments = APIHTTP + "/Api/Moments/getFocusMoments";//获取关注动态列表
    public static final String addOrCancelUserFocus = APIHTTP + "/Api/Member/addOrCancelFocus";//(用户)关注和取消关注
    public static final String likeMoments = APIHTTP + "/Api/Moments/likeMoments";//点赞/取消点赞
    public static final String momentsDetail = APIHTTP + "/Api/Moments/momentsDetail";//动态详情
    public static final String submitComment = APIHTTP + "/Api/Moments/commentMoments";//提交评论
    public static final String getSomebodyMoments = APIHTTP + "/Api/Moments/getSomebodyMoments";//获取某人的动态
    public static final String delMoments = APIHTTP + "/Api/Moments/delMoments";//删除某条动态
    public static final String getSomebodyAlbum = APIHTTP + "/Api/Moments/getSomebodyAlbum";//获取某人的相册信息

    public static final String getMyGroup = APIHTTP + "/Api/Member/friendGroup";//我的分组
    public static final String friendGroupMember = APIHTTP + "/Api/Member/friendGroupMember";//我的分组及分组下好友
    public static final String getMyGroupMember = APIHTTP + "/Api/Member/follows";//我的分组成员
    public static final String addOrEditFriendGroup = APIHTTP + "/Api/Member/addOrEditFriendGroup";//新建或编辑分组
    public static final String moveToSpecifiedGroup = APIHTTP + "/Api/Member/moveToSpecifiedGroup";//移动用户到指定分组

    public static final String deleteFriendGroup = APIHTTP + "/Api/Member/deleteFriendGroup";//删除分组

    public static final String phoneFriends = APIHTTP + "/Api/Member/phoneFriends";//手机通讯录好友

    public static final String getAlumnInfo = APIHTTP + "/Api/AlumniDirectory/info";//校友录信息
    public static final String locateToMe = APIHTTP + "/Api/AlumniDirectory/locateToMe";//校友录定位到我
    public static final String getAlumnMember = APIHTTP + "/Api/AlumniDirectory/people";//校友录院系人员查询

    public static final String searchUsers = APIHTTP + "/Api/Member/searchUsers";//搜索用户

    public static final String getSomebodyInfo = APIHTTP + "/Api/Moments/getSomebodyInfo";//某人的主页头部

    public static final String getRecommendFriends = APIHTTP + "/Api/AlumniDirectory/recommendFriends";//获取推荐好友列表

    public static final String getNearbyMoment = APIHTTP + "/Api/Moments/getNearby ";//获取附近的动态列表

    public static final String getSecondHandMarket = APIHTTP + "/Api/Moments/secondHandMarket";//获取二手市场动态列表

    public static final String getQuestions = APIHTTP + "/Api/Moments/getQuestions";//获取学弟学妹提问动态列表

    public static final String getGroupInfo = APIHTTP + "/Api/Group/getGroupInfo";//获取群资料详情
    public static final String applyGroup = APIHTTP + "/Api/Group/applyGroup";//申请加群

    public static final String quitGroup = APIHTTP + "/Api/Group/quitGroup";//退出群组
    public static final String dropGroup = APIHTTP + "/Api/Group/delete";//解散群组

    public static final String createOrUpdateGroup = APIHTTP + "/Api/Group/addOrEdit";//新建群组
    public static final String updateAvatar = APIHTTP + "/Api/Member/updateAvatar";//修改头像

    public static final String rechargePoints = APIHTTP + "/Api/Order/rechargePoints";//充值学分

    public static final String updateCover = APIHTTP + "/Api/Moments/updateCover";//修改个人主页封面图片

    public static final String addOrCancelBlack = APIHTTP + "/Api/Member/addOrCancelBlack";//拉黑或取消拉黑

    public static final String aboutUs = APIHTTP + "/Api/Index/about";//关于我们

    public static final String checkUpdate = APIHTTP + "/Api/Index/checkUpdate";//检查版本更新
    public static final String fillInviteCode = APIHTTP + "/Api/Member/fillInInvitationCode";//填写邀请码
    public static final String applyMedia = APIHTTP + "/Api/Index/applyMedia/member_id/";//申请自媒体

    public static final String searchGroup = APIHTTP + "/Api/Group/getList";//搜索群组

    public static final String groupNotice = APIHTTP + "/Api/Group/groupNotice";//群组通知

    public static final String auditMember = APIHTTP + "/Api/Group/auditMember";//审核加群

    public static final String getGroupMemberList = APIHTTP + "/Api/Group/getGroupMemberList";//群成员列表

    public static final String inviteFriends = APIHTTP + "/Api/Group/inviteFriends";//邀请好友进群

    public static final String removeMembers = APIHTTP + "/Api/Group/deleteGroupMember";//移除群组成员

    public static final String  momentArticle= APIHTTP + "/Api/Index/moments?id=";//文章类圈子拼接的地址

    public static final String commentList = APIHTTP + "/Api/Moments/commentList";//动态评论列表

    public static final String loginByWx = APIHTTP + "/Api/Public/loginByThirdParty";//微信登录

    public static final String addToadmin = APIHTTP + "/Api/Group/addGroupAdmin";//添加为管理员

    public static final String removeFromadmin = APIHTTP + "/Api/Group/delGroupAdmin";//移出管理员

    public static final String muteAllUrl = APIHTTP + "/Api/Group/disableSendMsg";//禁言

    public static final String getMuteStatus = APIHTTP + "/Api/Group/getDisableSendMsg";//禁言状态

    public static final String getAllConstants = APIHTTP + "Api/Public/getMemberExt";//获取所有的联系人以及群组

    public static final String changeRemarkUrl = APIHTTP + "/Api/Member/setFriendRemark";//修改备注

    public static final String getRemarkList = APIHTTP + "/Api/Member/getFriendRemark";//获取我的备注好友列表

    public static final String getNewFriedsApply = APIHTTP + "/Api/Member/getApplyFriends";//获取好友申请列表

    public static final String addNewFriends = APIHTTP + "/Api/Member/agreeFriend";//同意添加好友

    public static final String applyAddNewFriends = APIHTTP + "/Api/Member/applyFriend";//申请添加好友

    public static final String deleteFriends = APIHTTP + "/Api/Member/deleteFriend";//删除好友

    public static final String getAreaCodeUrl = APIHTTP + "/Api/Public/getAreaCode";//获取国际区号

    public static final String getGroupCode = APIHTTP + "/Api/Group/getGroupCode";//获取群二维码

    public static final String sendMultiMsg = APIHTTP + "/Api/Group/mass";//群发消息

    public static final String changeGroupOwner = APIHTTP + "/Api/Group/changeGroupOwner";//群组转让

    public static final String getGroupNoticeNum = APIHTTP + "/Api/Group/haveNewApplyGroupRecord";//群组通知数量

    public static final String sysNotice = APIHTTP + "/Api/Index/pushDetail/id/";//系统通知





}
