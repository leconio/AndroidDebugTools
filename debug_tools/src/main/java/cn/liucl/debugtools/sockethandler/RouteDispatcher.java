package cn.liucl.debugtools.sockethandler;

import cn.liucl.debugtools.route.Route;
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

    public Route dispatch(HttpParamsParser.Request content) {
        return null;
    }
}
