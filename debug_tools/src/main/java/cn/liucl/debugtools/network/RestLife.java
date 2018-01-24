package cn.liucl.debugtools.network;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * 请求器的小型切面
 * Created by spawn on 17-10-24.
 */

public interface RestLife {

    /**
     * 请求开始之前
     */
    void onRequestStart();

    /**
     * 请求马上开始
     */
    void onRequest(String requestParams, HttpURLConnection urlConnection) throws IOException;

    /**
     * 请求完成
     *
     * @param result 结果
     */
    void onRequestFinish(String result);

}
