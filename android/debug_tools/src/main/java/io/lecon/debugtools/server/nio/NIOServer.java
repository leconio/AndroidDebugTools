package io.lecon.debugtools.server.nio;

import android.content.Context;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import io.lecon.debugtools.route.RouteDispatcher;
import io.lecon.debugtools.server.HttpParamsParser;
import io.lecon.debugtools.server.Server;
import io.lecon.debugtools.server.resp.Response;

public class NIOServer implements Server, Runnable {

    private static int BUFF_SIZE = 1024;
    private static int TIME_OUT = 2000;
    private final Context mContext;
    private final int mProtNumber;
    private Selector selector = null;


    public NIOServer(Context context, int protNumber) {
        mContext = context;
        mProtNumber = protNumber;
    }

    @Override
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void stop() {
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        TCPProtocol protocol = new EchoSelectorProtocol(mContext, BUFF_SIZE);
        //声明一个server socket channel,而且是非阻塞的。
        ServerSocketChannel ssc;
        try {
            //声明一个selector
            selector = SelectorProvider.provider().openSelector();
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);

            InetSocketAddress isa = new InetSocketAddress(mProtNumber);
            ssc.socket().bind(isa);
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Set readyKeys = selector.selectedKeys();
                Iterator i = readyKeys.iterator();
                while (i.hasNext()) {
                    SelectionKey sk = (SelectionKey) i.next();
                    i.remove();

                    if (sk.isAcceptable()) {
                        protocol.handleAccept(sk);
                    } else if (sk.isValid() && sk.isReadable()) {
                        protocol.handleRead(sk);
                    } else if (sk.isValid() && sk.isWritable()) {
                        protocol.handleWrite(sk);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    static class HandleMsg implements Runnable {
        private final Context mContext;
        SelectionKey selectionKey;
        ByteBuffer byteBuffer;

        HandleMsg(Context mContext, SelectionKey selectionKey, ByteBuffer byteBuffer) {
            this.mContext = mContext;
            this.selectionKey = selectionKey;
            this.byteBuffer = byteBuffer;
        }

        @Override
        public void run() {
            EchoClient echoClient = (EchoClient) selectionKey.attachment();

            try {
                CharBuffer decode = EchoSelectorProtocol.CharsetHelper.decode(byteBuffer);
                HttpParamsParser.Request parse = HttpParamsParser.parse(decode.toString());
                Response resp = RouteDispatcher.getInstance(mContext).dispatch(parse);
                echoClient.enqueue(ByteBuffer.wrap(resp.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //we've enqueued data to be written to the client,we must
            //not set interest in OP_WRITE
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            selectionKey.selector().wakeup();
        }
    }


    static class EchoClient {
        private LinkedList<ByteBuffer> outq;

        EchoClient() {
            outq = new LinkedList<>();
        }

        public LinkedList<ByteBuffer> getOutputQueue() {
            return outq;
        }

        public void enqueue(ByteBuffer bb) {
            outq.addFirst(bb);
        }
    }

}
