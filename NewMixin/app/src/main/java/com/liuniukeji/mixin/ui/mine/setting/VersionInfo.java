package com.liuniukeji.mixin.ui.mine.setting;

/**
 * 软件版本信息
 */
public class VersionInfo  {

    private AppVersion ANDROID_VERSION;

    static class AppVersion {
        /**
         * "code":"1", //类型：String  必有字段  备注：版本号
         * "name":"1.0.1", //类型：String  必有字段  备注：版本名称
         * "url":"www.pgyer.com/EtoG" //类型：String  必有字段  备注：下载地址
         */

        private String code;
        private String name;
        private String url;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public AppVersion getANDROID_VERSION() {
        return ANDROID_VERSION;
    }

    public void setANDROID_VERSION(AppVersion ANDROID_VERSION) {
        this.ANDROID_VERSION = ANDROID_VERSION;
    }
}
