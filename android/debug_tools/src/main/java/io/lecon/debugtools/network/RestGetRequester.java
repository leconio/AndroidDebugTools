package io.lecon.debugtools.network;

import android.webkit.CookieManager;


import java.io.IOException;
import java.net.HttpURLConnection;

import io.lecon.debugtools.Constants;

/**
 * 普通GET请求
 * Created by spawn on 17-10-24.
 */

public class RestGetRequester extends RestRequester {

    public RestGetRequester(String url) {
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
        urlConnection.setRequestMethod("GET");
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie(Constants.HOST);
        urlConnection.setRequestProperty("Cookie", cookie);
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
    }

    @Override
    public void onRequestFinish(String result) {

    }
}
