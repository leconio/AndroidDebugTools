package cn.liucl.debugtools.server;

/**
 * Created by spawn on 17-9-28.
 */

public class ByteResponse implements Response {

    private final byte[] bytes;

    public ByteResponse(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte[] getContent() {
        return bytes;
    }
}
