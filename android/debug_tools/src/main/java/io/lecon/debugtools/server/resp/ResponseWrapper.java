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
     * 拼接 HTTP Response
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

    public static void writeJson(Response resp) {
        StringBuilder writer = new StringBuilder();
        writeCommonRespHeader(writer);
        writer.append("Content-Type: application/json\n");
        writer.append("\r\n");
        byte[] bytes = writer.toString().getBytes();
        resp.appendHead(bytes);
    }

    /*
    if (new File(route.split("file")[1]).isDirectory()) {
        writer.append("Content-Disposition: attachment; filename=").append(route.substring(route.lastIndexOf("/") + 1)).append(".zip").append("\r\n");
    } else {
        writer.append("Content-Disposition: attachment; filename=").append(route.substring(route.lastIndexOf("/") + 1)).append("\r\n");
    }*/

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
