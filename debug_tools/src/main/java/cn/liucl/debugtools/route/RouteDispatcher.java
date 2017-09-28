package cn.liucl.debugtools.route;

import cn.liucl.debugtools.server.HttpParamsParser;

/**
 * Created by spawn on 17-9-28.
 */

public class RouteDispatcher {

    private RouteDispatcher() {
    }
    private static RouteDispatcher sRouteDispatcher;
    public static RouteDispatcher getInstance(){
        if (sRouteDispatcher == null) {
            synchronized (RouteDispatcher.class) {
                if (sRouteDispatcher == null) {
                    sRouteDispatcher = new RouteDispatcher();
                }
            }
        }
        return sRouteDispatcher;
    }

    public Route dispatch(HttpParamsParser.Request parse) {
        Route route = null;
        String requestURI = parse.getRequestURI();
        String[] urlSplit = requestURI.split("/");
        if (urlSplit.length == 1 || urlSplit[1].contains("debug")) {

        }
        if (urlSplit[1].contains("error")) {
            route = new ErrorRoute();
        }
        return route;
    }
}
