package io.lecon.debugtools.route;

import android.content.Context;

import java.io.IOException;

import io.lecon.debugtools.Utils.Utils;
import io.lecon.debugtools.server.HttpParamsParser;
import io.lecon.debugtools.server.Result;
import io.lecon.debugtools.server.resp.ByteResponse;
import io.lecon.debugtools.server.resp.JsonResponse;
import io.lecon.debugtools.server.resp.Response;

/**
 * Created by spawn on 17-9-28.
 */

public class RouteDispatcher {

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
            return null;
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

        if (urlSplit[1].contains("asset")) {
            return new ByteResponse(Utils.loadAssetContent(requestURI, mContext.getAssets()));
        }

        //资源处理
        return new JsonResponse(Result.RESULT_ERROR);
    }
}
