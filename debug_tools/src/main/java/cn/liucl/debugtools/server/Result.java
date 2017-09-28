package cn.liucl.debugtools.server;

/**
 * Created by spawn on 17-9-28.
 */

public class Result {

    public static final String OK = "ok";

    private boolean isSuccessful;
    private byte[] content;
    private String message;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
