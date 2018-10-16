package cn.liucl.debugtools.disk;

public interface DiskHelper {

    /**
     * 列出目录
     * @param type 类型 sd，内部路径
     * @param path 路径
     */
    String getFolderList(String type, String path) throws DiskException;
}
