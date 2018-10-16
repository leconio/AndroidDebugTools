package io.lecon.debugtools.route;

import io.lecon.debugtools.server.HttpParamsParser;
import io.lecon.debugtools.server.Result;

/**
 * Created by spawn on 17-9-28.
 */

public interface Route {

    /**
     * 路由处理
     *
     * @return 结果
     * @param request
     */
    Result process(HttpParamsParser.Request request);
}
