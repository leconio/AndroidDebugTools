package cn.liucl.debugtools.disk;

public class DiskException extends Exception {

    public static final DiskException FILE_NOT_FOUND = new DiskException("没有找到这个文件");
    public static final DiskException NOT_A_FOLDER = new DiskException("目标不是一个路径");
    public static final DiskException NO_PERMISSION = new DiskException("路径没有权限或者SD卡未挂载");

    public DiskException(String message) {
        super(message);
    }
}