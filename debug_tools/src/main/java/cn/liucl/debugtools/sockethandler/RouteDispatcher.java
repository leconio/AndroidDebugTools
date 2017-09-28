package cn.liucl.debugtools.sockethandler;

import android.util.Log;

import java.util.Arrays;

import cn.liucl.debugtools.route.ErrorRoute;
import cn.liucl.debugtools.route.Route;
import cn.liucl.debugtools.server.HttpParamsParser;

import static cn.liucl.debugtools.DebugTools.TAG;

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
        Log.i(TAG, "dispatch: " + Arrays.toString(urlSplit));
        if (urlSplit.length == 1 || urlSplit[1].contains("debug")) {

        }
        if (urlSplit[1].contains("error")) {
            route = new ErrorRoute();
        }
        return route;
    }
}
