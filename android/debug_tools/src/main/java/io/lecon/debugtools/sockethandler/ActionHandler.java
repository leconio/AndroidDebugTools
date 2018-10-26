package io.lecon.debugtools.sockethandler;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import io.lecon.debugtools.route.RouteDispatcher;
import io.lecon.debugtools.server.HttpParamsParser;
import io.lecon.debugtools.server.resp.Response;
import io.lecon.debugtools.utils.Utils;

import static io.lecon.debugtools.DebugTools.TAG;

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
            output.write(resp.getContent());

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
     * @param inStream
     * @return 字节数组
     * @throws Exception
     * @功能 读取流
     */
    private static byte[] readStream(InputStream inStream) throws IOException {
        int count = 0;
        while (count == 0) {
            count = inStream.available();
        }
        byte[] b = new byte[count];
        inStream.read(b);
        return b;
    }
}
