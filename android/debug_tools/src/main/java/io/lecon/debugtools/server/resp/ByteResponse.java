package io.lecon.debugtools.server.resp;

/**
 * Created by spawn on 17-9-28.
 */

public class ByteResponse extends BaseResponse {

    public ByteResponse(byte[] bytes) {
        this.bytes = bytes;
    }
}
