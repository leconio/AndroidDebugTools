package cn.liucl.debugtools.route;

import cn.liucl.debugtools.server.Result;

/**
 * Created by spawn on 17-9-28.
 */

public class ErrorRoute implements Route {

    @Override
    public Result process() {
        String html = "{\"info\":\"<h1>:) 美好的一天</h1>\"}";
        Result result = new Result();
        result.setContent(html.getBytes());
        result.setSuccessful(true);
        result.setMessage("ok");
        return result;
    }

}
