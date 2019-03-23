package com.liuniukeji.mixin.util;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;

/**
 * 阿里OSS操作工具
 */
public class OSSOperUtils {
    private static OSS oss;
    private static OSSOperUtils utils;

    /**
     * GetObjectRequest get = new GetObjectRequest("duia-log", name);
     * <p>
     * String endpoint = "https://oss-cn-beijing.aliyuncs.com";
     * <p>
     * // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
     * OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("qgR150FXSbdnCi5e", "wi4jUJvFgKZXkPp63vCY8nA849GpLT");
     */

    /**
     * 运行sample前需要配置以下字段为有效的值
     */
    private static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    private static final String accessKeyId = "LTAIIgG6FlUU1XZm";
    private static final String accessKeySecret = "87ucJsjsSwmCpkGDmbeZA5AkBXcH2F";
    private static final String testBucket = "mixins";
    public static final String AliYunOSSURLFile = "http://mixins.oss-cn-beijing.aliyuncs.com/";

    public static OSSOperUtils newInstance(Context context) {
        if (null == utils) {
            utils = new OSSOperUtils();
        }
        if (null == oss) {
            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
            ClientConfiguration conf = new ClientConfiguration();
            // 连接超时，默认15秒
            conf.setConnectionTimeout(150 * 1000);
            // socket超时，默认15秒
            conf.setSocketTimeout(150 * 1000);
            // 最大并发请求书，默认5个
            conf.setMaxConcurrentRequest(5);
            // 失败后最大重试次数，默认2次
            conf.setMaxErrorRetry(2);
            OSSLog.enableLog();
            oss = new OSSClient(context, endpoint, credentialProvider, conf);
        }
        return utils;
    }

    //上传
    @SuppressWarnings("all")
    public void putObjectMethod(final String uploadObject, final String uploadFilePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new PutObjectSamples(oss, testBucket, uploadObject, uploadFilePath).asyncPutObjectFromLocalFile();
            }
        }).start();

    }

}
