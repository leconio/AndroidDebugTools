package io.lecon.debugtools.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Created by spawn on 17-10-24.
 */

public class RestPostRequester extends RestRequester {

    public RestPostRequester(String url) {
        super(url);
    }

    @Override
    protected void onThreadRunning() throws IOException {
        super.onThreadRunning();
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequest(String requestParams, HttpURLConnection urlConnection) throws IOException {
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
        urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
        if (requestParams != null) {
            OutputStream os = urlConnection.getOutputStream();
            os.write(requestParams.getBytes());
            os.flush();
        }
    }

    @Override
    public void onRequestFinish(String result) {

    }
}
