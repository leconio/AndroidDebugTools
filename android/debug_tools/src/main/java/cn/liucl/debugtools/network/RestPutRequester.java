package cn.liucl.debugtools.network;

import android.webkit.CookieManager;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import cn.liucl.debugtools.Constants;

/**
 * Created by 01369596 on 2017/11/2.
 */

public class RestPutRequester extends RestRequester{
    public RestPutRequester(String url) {
        super(url);
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequest(String requestParams, HttpURLConnection urlConnection) throws IOException {
        urlConnection.setRequestMethod("PUT");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie(Constants.HOST);
        urlConnection.setRequestProperty("Cookie", cookie);
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Charset", "UTF-8");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
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
