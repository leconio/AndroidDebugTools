package io.lecon.debugtools.sockethandler;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by spawn on 17-9-28.
 */

public interface Handler {

    /**
     * 处理这次Socket请求
     * @param socket
     * @throws IOException
     */
    void handle(Socket socket) throws IOException;

}
