package io.lecon.debugtools.server;


import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.lecon.debugtools.sockethandler.ActionHandler;
import io.lecon.debugtools.utils.Utils;

/**
 * Created by spawn on 15/11/16.
 */
public class ClientServer implements Runnable, Server {

    private static final String TAG = "ClientServer";

    private final int mPort;

    private boolean mIsRunning;

    private ServerSocket mServerSocket;

    private final ActionHandler mRequestHandler;

    private ExecutorService tp = new ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors(),
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());


    public ClientServer(Context context, int port) {
        mRequestHandler = new ActionHandler(context);
        mPort = port;
    }

    @Override
    public void start() {
        mIsRunning = true;
        new Thread(this).start();
    }

    @Override
    public void stop() {
        try {
            mIsRunning = false;
            if (null != mServerSocket) {
                mServerSocket.close();
                tp.shutdown();
                mServerSocket = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing the server socket.", e);
        }
    }

    @Override
    public void run() {
        try {
            mServerSocket = new ServerSocket(mPort);
            Log.i(TAG, "StartServer Ip: http://" + Utils.getIP() + ":" + mPort);
            while (mIsRunning) {
                final Socket socket = mServerSocket.accept();
                tp.submit(new Runnable() {
                    @Override
                    public void run() {
                        mRequestHandler.handle(socket);
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (SocketException e) {
            // The server was stopped; ignore.
        } catch (IOException e) {
            Log.e(TAG, "Web server error.", e);
        } catch (Exception ignore) {

        }
    }

    public boolean isRunning() {
        return mIsRunning;
    }
}
