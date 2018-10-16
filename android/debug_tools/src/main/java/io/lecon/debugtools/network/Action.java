package io.lecon.debugtools.network;

/**
 * Created by spawn on 17-10-24.
 */

public interface Action {

    /**
     * 执行网络请求
     *
     * @param json 请求参数
     * @param t    回调
     */
    void execute(String json, Callback t);

    /**
     * 无参数执行网络请求
     *
     * @param t 回调
     */
    void execute(Callback t);
}
