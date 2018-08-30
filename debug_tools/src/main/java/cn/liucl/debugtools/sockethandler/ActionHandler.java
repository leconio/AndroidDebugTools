package cn.liucl.debugtools.sockethandler;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import cn.liucl.debugtools.Utils;
import cn.liucl.debugtools.route.RouteDispatcher;
import cn.liucl.debugtools.server.HttpParamsParser;
import cn.liucl.debugtools.server.resp.Response;

import static cn.liucl.debugtools.DebugTools.TAG;

/**
 * Created by spawn on 17-9-28.
 */

public class ActionHandler implements Handler {

    private Context mContext;

    public ActionHandler(Context context) {
        mContext = context;
    }

    @Override
    public void handle(Socket socket) throws IOException {
        BufferedReader reader = null;
        PrintStream output = null;
        try {
            // 解析HTTP请求
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            String url = "";
            while (!TextUtils.isEmpty(line = reader.readLine())) {
                String[] firstLine = line.split(" ");
                url = firstLine[1];
                break;
            }
            output = new PrintStream(socket.getOutputStream());
            HttpParamsParser.Request parse = HttpParamsParser.parse(url);
            Log.i(TAG, "Url: " + parse);
            Response resp = RouteDispatcher.getInstance(mContext).dispatch(parse);
            writeContent(output, resp, parse.getRequestURI());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != output) {
                    output.close();
                }
                if (null != reader) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 主动模式使用
     * @param os 写回去
     * @param url actionUrl
     */
    public void handle(OutputStream os,String url) throws IOException {
        PrintStream output = new PrintStream(os);
        HttpParamsParser.Request parse = HttpParamsParser.parse(url);
        Log.i(TAG, "Url: " + parse);
        Response resp = RouteDispatcher.getInstance(mContext).dispatch(parse);
        writeContent(output, resp, parse.getRequestURI());
    }

    /**
     * 写入响应报文
     *
     * @param output 写入流
     * @param resp   写入内容
     * @param route  访问路由信息
     */
    private void writeContent(PrintStream output, Response resp, String route) throws IOException {
        byte[] bytes = null;
        if (resp == null || null == (bytes = resp.getContent())) {
            writeServerError(output);
            return;
        }

        String contentType = "Content-Type: " + Utils.getMimeType(route);

        // Send out the content.
        output.println("HTTP/1.0 200 OK");

        if (route.contains("downloadFile")) {
            output.println("Content-Disposition: attachment; filename=" + route.substring(route.lastIndexOf("/") + 1));
        } else {
            contentType = "Content-Type: application/json";
            output.println("Content-Length: " + bytes.length);
        }
        output.println(contentType);
        output.println();
        output.write(bytes);
        output.flush();
    }

    private void writeServerError(PrintStream output) throws IOException {
        output.println("HTTP/1.0 404 Not Found");
        output.flush();
    }
}
