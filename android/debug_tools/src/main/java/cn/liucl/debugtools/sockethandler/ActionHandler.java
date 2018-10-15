package cn.liucl.debugtools.sockethandler;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
    public void handle(Socket socket) {
        InputStream is = null;
        PrintStream output = null;
        try {
            // 解析HTTP请求
            is = socket.getInputStream();
            byte[] bytes = readStream(is);
            String body = new String(bytes);
            output = new PrintStream(socket.getOutputStream());
            HttpParamsParser.Request parse = HttpParamsParser.parse(body);
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
                if (null != is) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @功能 读取流
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws IOException {
        int count = 0;
        while (count == 0) {
            count = inStream.available();
        }
        byte[] b = new byte[count];
        inStream.read(b);
        return b;
    }

    /**
     * 主动模式使用
     * //FIXME
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
        output.println("Access-Control-Allow-Origin: *");
        output.println("Access-Control-Allow-Credentials: true");
        output.println("Access-Control-Allow-Methods: *");
        output.println("Access-Control-Allow-Headers: Content-Type,Access-Token");
        output.println("Access-Control-Expose-Headers: *");

        if (route.contains("file") || route.contains("asset")) {
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
