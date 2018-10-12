package cn.liucl.debugtools.network;

import android.util.Log;
import android.webkit.CookieManager;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.liucl.debugtools.Constants;

import static cn.liucl.debugtools.DebugTools.TAG;

/**
 * Created by spawn on 17-10-24.
 */

public abstract class RestRequester extends Thread implements RestLife, Action {

    private String mReqUrl;
    private HttpURLConnection urlConnection;
    private String requestParams;
    private Callback callback;

    public RestRequester(String url) {
        mReqUrl = url;
    }

    @Override
    public void run() {
        try {
            onThreadRunning();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onThreadRunning() throws IOException {
        onRequestStart();
        urlConnection = (HttpURLConnection) new URL(mReqUrl).openConnection();
        urlConnection.setReadTimeout(5000);
        urlConnection.setConnectTimeout(5000);
        onRequest(requestParams, urlConnection);
        sendRequest(callback);
    }

    @Override
    public void execute(String json, Callback t) {
        this.requestParams = json;
        this.callback = t;
        start();
    }

    @Override
    public void execute(Callback t) {
        this.requestParams = null;
        this.callback = t;
        start();
    }

    private void sendRequest(Callback t) {
        String result = null;
        try {
            urlConnection.connect();
            if (urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() < 400) {
                String headerField = urlConnection.getHeaderField("Set-Cookie");
                if (headerField != null) {
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.acceptCookie();
                    cookieManager.setCookie(Constants.HOST, headerField);
                }

                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                result = processResult(is);
                if (t != null) {
                    t.onSuccess(result);
                }
            } else {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getErrorStream();
                // 创建字节输出流对象
                result = processResult(is);
                if (t != null) {
                    t.onError(new IOException(result));
                }
                Log.e(TAG, "sendRequest: " + result);
            }
        } catch (IOException e) {
            if (t != null) {
                t.onError(e);
            }
            e.printStackTrace();
        } finally {
            onRequestFinish(result);
        }
    }

    private String processResult(InputStream is) throws IOException {
        // 创建字节输出流对象
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 定义读取的长度
        int len = 0;
        // 定义缓冲区
        byte buffer[] = new byte[1024];
        // 按照缓冲区的大小，循环读取
        while ((len = is.read(buffer)) != -1) {
            // 根据读取的长度写入到os对象中
            baos.write(buffer, 0, len);
        }
        // 释放资源
        is.close();
        baos.close();
        // 返回字符串
        return new String(baos.toByteArray());
    }
}
