package io.lecon.debugtools.server.nio;

import android.content.Context;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EchoSelectorProtocol implements TCPProtocol {

    private final Context mContext;
    private int bufSize; // 缓冲区的长度
    private ExecutorService tp = Executors.newCachedThreadPool();


    public EchoSelectorProtocol(Context mContext, int bufSize) {
        this.mContext = mContext;
        this.bufSize = bufSize;
    }

    @Override
    public void handleAccept(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel;
        try {
            //获取客户端的channel
            clientChannel = server.accept();
            clientChannel.configureBlocking(false);

            //register the channel for reading
            SelectionKey clientKey = clientChannel.register(key.selector(), SelectionKey.OP_READ);
            //Allocate an EchoClient instance and attach it to this selection key.
            NIOServer.EchoClient echoClient = new NIOServer.EchoClient();
            clientKey.attach(echoClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleRead(final SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(bufSize);
        int len;

        try {
            len = channel.read(buf);
            if (len < 0) {
                disconnect(key);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            disconnect(key);
            return;
        }
        buf.flip();
        tp.execute(new NIOServer.HandleMsg(mContext, key, buf));
    }

    @Override
    public void handleWrite(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        NIOServer.EchoClient echoClient = (NIOServer.EchoClient) key.attachment();
        LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();

        ByteBuffer bb = outq.getLast();
        try {
            int len = channel.write(bb);
            if (len == -1) {
                disconnect(key);
                return;
            }
            if (bb.remaining() == 0) {
                outq.removeLast();
            }
        } catch (Exception e) {
            e.printStackTrace();
            disconnect(key);
        }

        if (outq.size() == 0) {
            key.interestOps(SelectionKey.OP_READ);
        }
    }


    private void disconnect(SelectionKey sk) {
        SocketChannel sc = (SocketChannel) sk.channel();
        try {
            sc.finishConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static class CharsetHelper {
        private static final String UTF_8 = "UTF-8";
        private static CharsetEncoder encoder = Charset.forName(UTF_8).newEncoder();
        private static CharsetDecoder decoder = Charset.forName(UTF_8).newDecoder();

        private CharsetHelper() {
        }

        public static ByteBuffer encode(CharBuffer in) throws CharacterCodingException {
            return encoder.encode(in);
        }

        public static CharBuffer decode(ByteBuffer in) throws CharacterCodingException {
            return decoder.decode(in);
        }
    }


}