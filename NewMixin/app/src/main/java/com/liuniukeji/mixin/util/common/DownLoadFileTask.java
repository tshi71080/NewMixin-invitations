package com.liuniukeji.mixin.util.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadFileTask {

    private Handler mHandler = null;

    public DownLoadFileTask(Handler handler) {
        this.mHandler = handler;
    }

    /**
     * @param path
     * @param filepath
     * @return
     * @throws Exception
     */
    public File getFile(String path, File filepath) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        if (conn.getResponseCode() == 200) {
            int total = conn.getContentLength();
            int progress = 0;
            int value = 0;
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                progress += len;
                if (value != (int) ((double) progress / (double) total * 100)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("progress", value);

                    Message message = new Message();
                    message.what = 0;
                    message.setData(bundle);
                    mHandler.sendMessage(message);

                }
                value = (int) ((double) progress / (double) total * 100);
            }
            fos.flush();
            fos.close();
            is.close();

            return filepath;
        } else {
            Message message = new Message();
            message.what = 2;
            mHandler.sendMessage(message);
        }
        return null;
    }
}
