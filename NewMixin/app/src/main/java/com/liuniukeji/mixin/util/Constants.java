package com.liuniukeji.mixin.util;

import android.os.Environment;

/**
 * 常量字段配置
 */
public class Constants {
    public static final String APP_ID = "wx47e65c340a201918";
    public static final String APP_SECRET = "c5d19e107a5759b32319983615568ac6";

    public static final int REQUEST_REFRESH = 10;
    public static final int RESULT_REFRESH = 11;
    public static final int CODE_TIME = 60;
    public static final int BUFFER_TIME = 30;
    public static final int COUNT_SIZE = 10;

    public static final int GET_AREACODE_REQUESTCODE = 0x0001;
    public static final int GET_AREACODE_RESULTCODE = 0x0002;
    public static final int to_register_request_code = 10;
    public static final int to_register_result_code = 11;


    public static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/xyq/images";

    //用户信息
    public static final String USERINFO = "userInfo";
    public static String USERTYPE = "user_type";
    public static final String USERCONNECTINFOGROUP = "USERCONNECTINFOGROUP";
    public static final String USERCONNECTINFOFRIEND = "USERCONNECTINFOFRIEND";
    public static final String USERREMARKINFO = "USERREMARKINFO";
    public static final String ARECODE = "ARECODE";
    public static final String LOCATION_INFO = "LOCATION_INFO";

    //判断群组是否禁言
    public static final String disableSendMsg = "disableSendMsg";


    public enum USER_INFO {
        /**
         * 用户基本信息更新
         */
        UPDATE, REAL_NAME
    }

    public enum ALI_PAY_STATE {
        /**
         * 支付宝支付回调状态
         */
        ON_SUCCESS, ON_ERROR, OTHER
    }


    public enum EDIT_GROUP {
        /**
         * 编辑分组
         */
        ON_CHANGE,
        //添加好友
        ADD_FRIEND,
        IS_OPERATE
    }

    public enum ADD_CIRCLE {
        /**
         * 添加圈子
         */
        ON_CHANGE
    }

    public enum UPDATE_MOMENT {
        /**
         * 更新动态数据
         */
        ON_CHANGE
    }

    public enum MUTE_INFO {
        /**
         * 禁言
         */
        MUTE,UN_MUTE
    }


    public enum GROUP_INFO {
        /**
         * 更新数据
         */
        ON_CHANGE,DROP,CREATE,MEMBER
    }

}
