package cn.liucl.debugtools;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import cn.liucl.debugtools.route.RouteDispatcher;
import cn.liucl.debugtools.server.HttpParamsParser;
import cn.liucl.debugtools.server.resp.Response;

import static cn.liucl.debugtools.DebugTools.TAG;

public class ConnectionServices extends IntentService {

    public static final String CONNECT_IP = "connect_ip";
    public static final String CONNECT_PORT = "connect_port";

    private String mConnectIp;
    private int mConnectPort;

    public ConnectionServices() {
        super("host_model");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mConnectIp = intent.getStringExtra(CONNECT_IP);
        mConnectPort = intent.getIntExtra(CONNECT_PORT, 8088);
        processHeartbeatRequest();
    }

    /**
     * 开启心跳请求
     * 500ms 一次。（测试）
     */
    private void processHeartbeatRequest() {
        handleSocket("what can I do for you", new Callback() {
            @Override
            public void onResponse(String ret) {
                processResult(ret);
            }
        });
    }

    /**
     * 处理结果返回到服务器
     *
     * @param result
     */
    private void sendResultToServer(String result) {
        handleSocket(result, new Callback() {
            @Override
            public void onResponse(String ret) {
                processResult(ret);
            }
        });
    }

    /**
     * 处理服务器返回的结果
     *
     * @param ret 结果内容
     */
    private void processResult(String ret) {
        // 如接收到 "action" 则断开连接
        if (ret.contains("action")) {
            String[] action = ret.split(":");
            sendResultToServer(execAction(action[1]));
        }

        if (ret.equals("done!")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            processHeartbeatRequest();
        }
    }

    /**
     * 发送Socket
     *
     * @param outStr   发送内容
     * @param callback 回调
     */
    private void handleSocket(String outStr, Callback callback) {
        Socket socket = null;
        try {
            socket = new Socket(mConnectIp, mConnectPort);
            //读取服务器端数据
            DataInputStream input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(outStr);
            String ret = input.readUTF();
            Log.i(TAG, "服务器返回的数据是: " + ret);
            callback.onResponse(ret);
            out.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    socket = null;
                }
            }
        }
    }

    public interface Callback {
        void onResponse(String ret);
    }

    /**
     * 执行操作函数
     * // FIXME
     *
     * @param action
     */
    private String execAction(String action) {
        HttpParamsParser.Request parse = HttpParamsParser.parse(action);
        Log.i(TAG, "Url: " + parse);
        try {
            Response resp = RouteDispatcher.getInstance(this).dispatch(parse);
            return new String(resp.getContent());
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
