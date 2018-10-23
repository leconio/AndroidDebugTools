package io.lecon.debugtools.route;

import android.content.Context;

import java.io.IOException;

import io.lecon.debugtools.server.HttpParamsParser;
import io.lecon.debugtools.server.Result;
import io.lecon.debugtools.server.resp.ByteResponse;
import io.lecon.debugtools.server.resp.JsonResponse;
import io.lecon.debugtools.server.resp.Response;
import io.lecon.debugtools.utils.Utils;

/**
 * Created by spawn on 17-9-28.
 */

public class RouteDispatcher {

    public static final String WEB_FOLDER = "/debug-web";

    private Context mContext;

    private RouteDispatcher(Context mContext) {
        this.mContext = mContext;
    }

    private static RouteDispatcher sRouteDispatcher;

    public static RouteDispatcher getInstance(Context mContext) {
        if (sRouteDispatcher == null) {
            synchronized (RouteDispatcher.class) {
                if (sRouteDispatcher == null) {
                    sRouteDispatcher = new RouteDispatcher(mContext);
                }
            }
        }
        return sRouteDispatcher;
    }

    public Response dispatch(HttpParamsParser.Request request) throws IOException {
        Response response = null;
        String requestURI = request.getRequestURI();

        if ("/".equals(requestURI)) {
            requestURI = "/index.html";
            request.setRequestURI(requestURI);
        }

        String[] urlSplit = requestURI.split("/");
        if (urlSplit.length == 1 || urlSplit[1].contains("debug")) {
            if ("OPTIONS".equals(request.getMethod())) {
                return new Response() {
                    @Override
                    public byte[] getContent() {
                        return new byte[0];
                    }
                };
            }
        }

        // error-demo
        if (urlSplit[1].contains("error")) {
            Route errorRoute = new ErrorRoute();
            Result process = errorRoute.process(request);
            response = new JsonResponse(process);
            return response;
        }

        if (urlSplit[1].contains("db")) {
            Route dbRoute = new DbRoute(mContext);
            Result process = dbRoute.process(request);
            response = new JsonResponse(process);
            return response;
        }

        if (urlSplit[1].contains("disk")) {
            Route diskRoute = new DiskRoute(mContext);
            Result process = diskRoute.process(request);
            response = new JsonResponse(process);
            return response;
        }

        //SimpleHTTPServer
        if (urlSplit[1].contains("file")) {
            return new ByteResponse(Utils.loadFileContent(requestURI.split("file")[1]));
        }

        //资源处理
        return new ByteResponse(Utils.loadAssetContent(WEB_FOLDER + requestURI, mContext.getAssets()));
    }
}
