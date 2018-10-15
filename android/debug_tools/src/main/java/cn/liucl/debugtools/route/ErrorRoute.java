package cn.liucl.debugtools.route;

import cn.liucl.debugtools.server.HttpParamsParser;
import cn.liucl.debugtools.server.Result;

/**
 * Created by spawn on 17-9-28.
 */

public class ErrorRoute implements Route {

    @Override
    public Result process(HttpParamsParser.Request request) {
        Result result = new Result();
        result.setSuccessful(false);
        result.setMessage(request.getGetParameter("msg"));
        return result;
    }

}
