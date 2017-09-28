package cn.liucl.debugtools.route;

/**
 * Created by spawn on 17-9-28.
 */

public class ErrorRoute implements Route {

    @Override
    public byte[] getContent() {
        return "<h1>:( 出错误了<h1>".getBytes();
    }
}
