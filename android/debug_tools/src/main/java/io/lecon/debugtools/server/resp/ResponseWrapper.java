package io.lecon.debugtools.server.resp;

import io.lecon.debugtools.utils.Utils;

public class ResponseWrapper {

    public enum ResponseType {
        ASSETS,JSON,FILE,
    }

    private static void writeCommonRespHeader(StringBuilder writer) {
        writer.append("HTTP/1.0 200 OK\n");
        writer.append("Access-Control-Allow-Origin: *\n");
        writer.append("Access-Control-Allow-Credentials: true\n");
        writer.append("Access-Control-Allow-Methods: *\n");
        writer.append("Access-Control-Allow-Headers: Content-Type,Access-Token\n");
        writer.append("Access-Control-Expose-Headers: *\n");
    }

    /**
     * 为资源写头
     *
     * @param resp  写入内容
     * @param fileName 文件名称
     */
    public static void writeAssets(Response resp, String fileName) {
        StringBuilder writer = new StringBuilder();
        writeCommonRespHeader(writer);
        String contentType = Utils.getMimeType(fileName);
        if (contentType == null) {
            // 默认ContentType
            contentType = "text/html";
        }
        writer.append("Content-Type: ").append(contentType).append("\r\n");
        writer.append("\r\n");
        byte[] bytes = writer.toString().getBytes();
        resp.appendHead(bytes);
    }

    /**
     * 为Json写头
     * @param resp 响应体
     */
    public static void writeJson(Response resp) {
        StringBuilder writer = new StringBuilder();
        writeCommonRespHeader(writer);
        writer.append("Content-Type: application/json\n");
        writer.append("\r\n");
        byte[] bytes = writer.toString().getBytes();
        resp.appendHead(bytes);
    }

    /**
     * 为文件写头
     * @param resp 相应体
     * @param isFolder 是否文件夹
     * @param fileName 文件名
     */
    public static void writeFile(Response resp, boolean isFolder, String fileName) {
        StringBuilder writer = new StringBuilder();
        writeCommonRespHeader(writer);
        if (isFolder) {
            writer.append("Content-Disposition: attachment; filename=").append(fileName).append(".zip").append("\r\n");
        } else {
            writer.append("Content-Disposition: attachment; filename=").append(fileName).append("\r\n");
        }
        writer.append("\r\n");
        byte[] bytes = writer.toString().getBytes();
        resp.appendHead(bytes);
    }

    public static void write404(Response resp) {
        resp.appendHead("HTTP/1.0 404 Not Found\r\n".getBytes());
    }

}
