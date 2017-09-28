package cn.liucl.debugtools.sockethandler;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import cn.liucl.debugtools.Utils;
import cn.liucl.debugtools.route.Route;
import cn.liucl.debugtools.server.HttpParamsParser;

/**
 * Created by spawn on 17-9-28.
 */

public class DefaultHandler implements Handler {

    private Context mContext;

    public DefaultHandler(Context context) {
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
            String url="";
            while (!TextUtils.isEmpty(line = reader.readLine())) {
                String[] firstLine = line.split(" ");
                url = firstLine[1];
                break;
            }
            output = new PrintStream(socket.getOutputStream());
            HttpParamsParser.Request parse = HttpParamsParser.parse(url);
            Route route = RouteDispatcher.getInstance().dispatch(parse);
            writeContent(output,route.getContent(),parse.getRequestURI());
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
     * 写入响应报文
     * @param output 写入流
     * @param content 写入内容
     * @param route 访问路由信息
     * @throws IOException
     */
    private void writeContent(PrintStream output,String content,String route) throws IOException{
        byte[] bytes = content.getBytes();
        if (null == bytes) {
            writeServerError(output);
            return;
        }

        // Send out the content.
        output.println("HTTP/1.0 200 OK");
        output.println("Content-Type: " + Utils.getMimeType(route));

        if (route.contains("downloadFile")) {
            output.println("Content-Disposition: attachment; filename=" + route.substring(route.lastIndexOf("/") + 1));
        } else {
            output.println("Content-Length: " + bytes.length);
        }
        output.println();
        output.write(bytes);
        output.flush();
    }

    private void writeServerError(PrintStream output) {
        output.println("HTTP/1.0 500 Internal Server Error");
        output.flush();
    }
}
