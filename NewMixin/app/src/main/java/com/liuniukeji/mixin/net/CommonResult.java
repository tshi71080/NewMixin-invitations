package com.liuniukeji.mixin.net;

/**
 * 通用返回结果
 */
public class CommonResult {

    public int status;
    public String info;

    public CommonResult() {

    }

    public CommonResult(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
