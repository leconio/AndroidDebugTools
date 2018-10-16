package io.lecon.debugtools.network;

/**
 * Created by spawn on 17-10-24.
 */

/**
 * HTTP请求回调器
 */
public interface Callback {

    void onSuccess(String data);

    void onError(Throwable t);
}
