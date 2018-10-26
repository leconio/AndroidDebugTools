package io.lecon.debugtools.server.resp;

public abstract class BaseResponse implements Response {

    byte[] bytes;

    @Override
    public void appendHead(byte[] content) {
        byte[] another = new byte[content.length + this.bytes.length];
        System.arraycopy(content, 0, another, 0, content.length);
        System.arraycopy(bytes, 0, another, content.length, bytes.length);
        this.bytes = another;
    }

    @Override
    public byte[] getContent() {
        return bytes;
    }
}
