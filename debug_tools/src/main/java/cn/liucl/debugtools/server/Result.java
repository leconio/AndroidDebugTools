package cn.liucl.debugtools.server;

/**
 * Created by spawn on 17-9-28.
 */

public class Result {

    public static final String OK = "ok";

    private boolean isSuccessful;
    private String obj;
    private String message;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
