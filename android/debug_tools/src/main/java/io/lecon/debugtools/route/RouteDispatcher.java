package io.lecon.debugtools.route;

import android.content.Context;

import java.io.File;
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
                return new ByteResponse(Utils.loadFileContent(requestURI.split("file")[1]));
            }
        }
        //资源处理
        return new ByteResponse(Utils.loadAssetContent(WEB_FOLDER + requestURI, mContext.getAssets()));
    }

    /**
     * 拼接 HTTP Response
     *
     * @param resp  写入内容
     * @param route 访问路由信息
     */
    private void writeContent(Response resp, String route) {
        StringBuilder writer = new StringBuilder();
        if (null == resp.getContent()) {
            resp.appendHead("HTTP/1.0 404 Not Found\r\n".getBytes());
        } else {
            // Send out the content.
            writer.append("HTTP/1.0 200 OK\n");
            writer.append("Access-Control-Allow-Origin: *\n");
            writer.append("Access-Control-Allow-Credentials: true\n");
            writer.append("Access-Control-Allow-Methods: *\n");
            writer.append("Access-Control-Allow-Headers: Content-Type,Access-Token\n");
            writer.append("Access-Control-Expose-Headers: *\n");
            String[] split = route.split("/");
            String contentType;
            if (route.contains("file")) {
                if (new File(route.split("file")[1]).isDirectory()) {
                    writer.append("Content-Disposition: attachment; filename=").append(route.substring(route.lastIndexOf("/") + 1)).append(".zip").append("\r\n");
                } else {
                    writer.append("Content-Disposition: attachment; filename=").append(route.substring(route.lastIndexOf("/") + 1)).append("\r\n");
                }
                contentType = "Content-Type: application/octet-stream\r\n";
            } else if (split[split.length - 1].contains(".")) {
                contentType = "Content-Type: " + Utils.getMimeType(route) + "\r\n";
            } else if (split[0].contains("storage") || split[0].contains("database")) {
                contentType = "Content-Type: application/json\n";
            } else {
                contentType = "Content-Type: " + "text/html" + "\r\n";
            }

            writer.append(contentType);
            writer.append("\r\n");
        }
        byte[] bytes = writer.toString().getBytes();
        resp.appendHead(bytes);
    }
}
