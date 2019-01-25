package io.lecon.debugtools.route;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import io.lecon.debugtools.server.HttpParamsParser;
import io.lecon.debugtools.server.Result;
import io.lecon.debugtools.server.resp.ByteResponse;
import io.lecon.debugtools.server.resp.JsonResponse;
import io.lecon.debugtools.server.resp.Response;
import io.lecon.debugtools.server.resp.ResponseWrapper;
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
        Response response = preDispatch(request);
        if (request != null) {
            writeContent(response, request.getRequestURI());
        }
        return response;
    }

    public Response preDispatch(HttpParamsParser.Request request) throws IOException {
        Response response = null;
        String requestURI = request.getRequestURI();

        if ("/".equals(requestURI)) {
            requestURI = "/index.html";
            request.setRequestURI(requestURI);
        }

        // 解决跨域
        String[] urlSplit = requestURI.split("/");
        if (urlSplit.length == 1 || "debug".equals(urlSplit[1])) {
            if ("OPTIONS".equals(request.getMethod())) {
                return new Response() {
                    @Override
                    public byte[] getContent() {
                        return new byte[0];
                    }

                    @Override
                    public void appendHead(byte[] content) {

                    }

                    @Override
                    public ResponseWrapper.ResponseType getType() {
                        return null;
                    }
                };
            }
        }
        if (urlSplit.length > 1) {
            // error-demo
            if ("error".equals(urlSplit[1])) {
                Route errorRoute = new ErrorRoute();
                Result process = errorRoute.process(request);
                response = new JsonResponse(process);
                return response;
            }

            if ("db".equals(urlSplit[1])) {
                Route dbRoute = new DbRoute(mContext);
                Result process = dbRoute.process(request);
                response = new JsonResponse(process);
                return response;
            }

            if ("disk".equals(urlSplit[1])) {
                Route diskRoute = new DiskRoute(mContext);
                Result process = diskRoute.process(request);
                response = new JsonResponse(process);
                return response;
            }

            //SimpleHTTPServer
            if ("file".equals(urlSplit[1])) {
                return new ByteResponse(false, Utils.loadFileContent(requestURI.split("file")[1]));
            }
        }
        //资源处理
        return new ByteResponse(true, Utils.loadAssetContent(WEB_FOLDER + requestURI, mContext.getAssets()));
    }

    /**
     * 拼接头和body
     * 以后考虑用工厂实现
     *
     * @param resp
     * @param route
     */
    private void writeContent(Response resp, String route) {
        switch (resp.getType()) {
            case JSON:
                ResponseWrapper.writeJson(resp);
                break;
            case FILE:
                boolean isFolder = new File(route.split("file")[1]).isDirectory();
                String fileName = route.substring(route.lastIndexOf("/") + 1);
                ResponseWrapper.writeFile(resp, isFolder, fileName);
                break;
            case ASSETS:
            default:
                ResponseWrapper.writeAssets(resp, route);

        }
    }
}
