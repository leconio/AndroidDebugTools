package io.lecon.debugtools.route;

import io.lecon.debugtools.server.HttpParamsParser;
import io.lecon.debugtools.server.Result;

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
