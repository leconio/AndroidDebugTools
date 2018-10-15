package cn.liucl.debugtools.server;

/**
 * Created by spawn on 17-9-28.
 */

public class Result {

    public Result() {
    }

    public Result(boolean isSuccessful, String obj, String message) {
        this.isSuccessful = isSuccessful;
        this.obj = obj;
        this.message = message;
    }

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

    @Override
    public String toString() {
        return "Result{" +
                "isSuccessful=" + isSuccessful +
                ", obj='" + obj + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    static public Result ERROR_RESULT = new Result(false, "unknow error", "unknow error");
}
