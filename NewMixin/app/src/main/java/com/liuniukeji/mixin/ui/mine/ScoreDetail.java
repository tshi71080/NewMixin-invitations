package com.liuniukeji.mixin.ui.mine;

import java.util.List;

/**
 * 学分明细
 */
public class ScoreDetail {

    private List<ScoreDetailBean> list;
    private String total_num;


    public List<ScoreDetailBean> getList() {
        return list;
    }

    public void setList(List<ScoreDetailBean> list) {
        this.list = list;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    static class ScoreDetailBean {

        /**
         * "id":"66",//类型：String  必有字段  备注：明细id
         * "member_id":"215",//类型：String  必有字段  备注：用户id
         * "amount":"0.00",//类型：String  必有字段  备注：无
         * "red_packet":"0.00",//类型：String  必有字段  备注：无
         * "order_id":"0",//类型：String  必有字段  备注：无
         * "withdraw_id":"0",//类型：String  必有字段  备注：无
         * "points":"-20",//类型：String  必有字段  备注：无
         * "experience":"0",//类型：String  必有字段  备注：无
         * "change_time":"1530494448",//类型：String  必有字段  备注：变动时间
         * "desc":"购买会员",//类型：String  必有字段  备注：描述
         * "change_num":"-20",//类型：String  必有字段  备注：变动学分
         * "time":"2018/07/02" //类型：String  必有字段  备注：变动时间日期
         */

        private String id;
        private String member_id;
        private String amount;
        private String red_packet;
        private String order_id;
        private String withdraw_id;
        private String points;
        private String experience;
        private String change_time;
        private String desc;
        private String change_num;
        private String time;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRed_packet() {
            return red_packet;
        }

        public void setRed_packet(String red_packet) {
            this.red_packet = red_packet;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getWithdraw_id() {
            return withdraw_id;
        }

        public void setWithdraw_id(String withdraw_id) {
            this.withdraw_id = withdraw_id;
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

        public String getChange_time() {
            return change_time;
        }

        public void setChange_time(String change_time) {
            this.change_time = change_time;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getChange_num() {
            return change_num;
        }

        public void setChange_num(String change_num) {
            this.change_num = change_num;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

    }

}
