package com.liuniukeji.mixin.net;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpParams;

/**
 *
 */
public abstract class OkGoRequest {
    public static void get(String url, Object tag, String cacheKey, CacheMode cacheMode, AbsCallback okGoCallBack) {
        OkGo.get(url).tag(tag).cacheKey(cacheKey).cacheMode(cacheMode).execute(okGoCallBack);
    }

    public static void get(String url, Object tag, AbsCallback okGoCallBack) {
        OkGo.get(url).tag(tag).cacheMode(CacheMode.NO_CACHE).execute(okGoCallBack);
    }

    public static void post(String url, Object tag, String cacheKey, CacheMode cacheMode, HttpParams httpParams, AbsCallback okGoCallBack) {
        OkGo.post(url).tag(tag).cacheKey(cacheKey).cacheMode(cacheMode).params(httpParams).execute(okGoCallBack);
    }

    public static void post(String url, Object tag, HttpParams httpParams, AbsCallback okGoCallBack) {
        OkGo.post(url).tag(tag).cacheMode(CacheMode.NO_CACHE).params(httpParams).execute(okGoCallBack);
    }
}
