package com.liuniukeji.mixin.ui.mine;

/**
 * 用户个人信息
 * 单独获取更新本地存储数据使用
 */
public class UserInfo {

    /**
     * "id":"215",  //类型：String  必有字段  备注：用户id
     * "type":"2", //类型：String  必有字段  备注：1管理员、2用户，3老师
     * "nickname":"用户1529393197", //类型：String  必有字段  备注：无
     * "real_name":"颜如玉", //类型：String  必有字段  备注：真实姓名
     * "username":"mock", //类型：String  必有字段  备注：无
     * "email":"mock", //类型：String  必有字段  备注：无
     * "phone":"18315765138",//类型：String  必有字段  备注：手机号
     * "photo_path":"mock", //类型：String  必有字段  备注：头像地址
     * "sex":"2", //类型：String  必有字段  备注：性别: 1男 2女
     * "disabled":"0",//类型：String  必有字段  备注：用户状态:-1删除0正常1禁用
     * "birthday":"2009-02-14",//类型：String  必有字段  备注：生日
     * "token":"666666",//类型：String  必有字段  备注：用户token
     * "openid":"mock",//类型：String  必有字段  备注：无
     * "im_name":"mock",//类型：String  必有字段  备注：聊天账号
     * "im_password":"mock",//类型：String  必有字段  备注：聊天密码
     * "last_login_time":"1530340218",//类型：String  必有字段  备注：上次登录时间
     * "last_login_ip":"2130706433",  //类型：String  必有字段  备注：上次登录ip
     * "invite_code":"147J", //类型：String  必有字段  备注：邀请人的邀请码
     * "fans":"0",//类型：String  必有字段  备注：粉丝数量
     * "follows":"0",//类型：String  必有字段  备注：关注数量
     * "moments":"0",//类型：String  必有字段  备注：动态数量
     * "points":"30", //类型：String  必有字段  备注：学分
     * "experience":"40",  //类型：String  必有字段  备注：等级
     * "signature":"mock",//类型：String  必有字段  备注：个性签名
     * "vip_type":"1", //类型：String  必有字段  备注：0,普通用户；1，VIP
     * "vip_end_time":"2018-09-27", //类型：String  必有字段  备注：会员到期时间
     * "is_show_birthday":"0", //类型：String  必有字段  备注：是否展示生日0不展示1展示
     * "address":"mock", //类型：String  必有字段  备注：地址
     * "interests":"mock"//类型：String  必有字段  备注：兴趣爱好
     */

    private String id;
    private String type;
    private String nickname;
    private String real_name;
    private String username;
    private String email;
    private String phone;
    private String photo_path;
    private String sex;
    private String disabled;
    private String birthday;
    private String token;
    private String openid;
    private String im_name;
    private String im_password;
    private String last_login_time;
    private String last_login_ip;
    private String invite_code;
    private String fans;
    private String follows;
    private String moments;
    private String points;
    private String experience;
    private String signature;
    private String vip_type;
    private String vip_end_time;
    private String is_show_birthday;
    private String address;
    private String interests;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIm_name() {
        return im_name;
    }

    public void setIm_name(String im_name) {
        this.im_name = im_name;
    }

    public String getIm_password() {
        return im_password;
    }

    public void setIm_password(String im_password) {
        this.im_password = im_password;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }

    public String getMoments() {
        return moments;
    }

    public void setMoments(String moments) {
        this.moments = moments;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }

    public String getVip_end_time() {
        return vip_end_time;
    }

    public void setVip_end_time(String vip_end_time) {
        this.vip_end_time = vip_end_time;
    }

    public String getIs_show_birthday() {
        return is_show_birthday;
    }

    public void setIs_show_birthday(String is_show_birthday) {
        this.is_show_birthday = is_show_birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }
}
