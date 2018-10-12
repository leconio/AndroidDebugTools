package cn.liucl.debugtools.network;

import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import cn.liucl.debugtools.Constants;

/**
 * Socket 实现文件上传
 */
public class HttpUpload {

    public static boolean post(String username, String password, String httpUrl, Map<String, String> params, File[] files) throws IOException {
        final String BOUNDARY = "3e0d13a5b06b3f85"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志
        //计算 Content-Length
        int fileDataLength = 0;
        for (File fp : files) {
            StringBuilder httpFile = buildFileHeader(BOUNDARY, fp);
            fileDataLength += httpFile.toString().length();
            fileDataLength += fp.length();
        }
        StringBuilder httpText = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            httpText.append("--");
            httpText.append(BOUNDARY);
            httpText.append("\r\n");
            httpText.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
            httpText.append(entry.getValue());
            httpText.append("\r\n");
        }
        //计算传输给服务器的实体数据总长度 endline（最后的结束线）
        int dataLength = httpText.toString().getBytes().length + fileDataLength + endline.getBytes().length;

        // 构建请求
        URL url = new URL(httpUrl);
        String hostAddress = url.getHost();
        int port = Constants.PORT;
        Socket socket = new Socket(InetAddress.getByName(hostAddress), port);
        //数据由Android应用传到服务器，所以构建输出流
//        OutputStream outStream = System.out;
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());

        //Host请求头，末尾"\r\n" 是回车换行符
        String host = "Host: " + url.getHost() + ":" + port + "\r\n";
        outStream.write(host.getBytes());

        //完成第二个协议头的数据，可省略
        String accept = "Accept: */*\r\n";
        outStream.write(accept.getBytes());

        //原始Origin
        String origin = "Origin: " + url.getProtocol() + "://" + url.getHost() + ":" + port + "\r\n";
        outStream.write(origin.getBytes());

        //ContentType
        String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
        outStream.write(contenttype.getBytes());

        //内容的长度
        String contentlength = "Content-Length: " + dataLength + "\r\n";
        System.out.println("dataLength====" + dataLength);
        outStream.write(contentlength.getBytes());

        //Connection请求头
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());

        //认证
        String encoding = EncodeUtils.base64Encode2String((username + ":" + password).getBytes());
        String auth = "Authorization: Basic " + encoding + "\r\n";
        outStream.write(auth.getBytes());

        //跨域问题
        String xat = "X-Atlassian-Token: no-check\r\n";
        outStream.write(xat.getBytes());

        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());

        //头部构建完毕 空行

        //写POST文本
        outStream.write(httpText.toString().getBytes());

        //写POST文件
        for (File fp : files) {
            StringBuilder httpFile = buildFileHeader(BOUNDARY, fp);
            //写Http部分
            outStream.write(httpFile.toString().getBytes());
            //写文件部分
            DataInputStream in = new DataInputStream(new FileInputStream(fp));
            int length = 0;
            byte[] bufferOut = new byte[10240000];
            while ((length = in.read(bufferOut)) != -1) {
                outStream.write(bufferOut, 0, length);
            }
            outStream.write("\r\n".getBytes());
        }
        //下面发送数据结束标志，表示数据已经结束，此时服务器会给客户端返回一个响应信息
        outStream.write(endline.getBytes());

        //从服务端读取响应状态，所以是InputStream
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        boolean uploadSucc = false;
        String buffer = null;
        while (!(buffer = reader.readLine()).equals("0")) {
            System.out.println("buffer === "+buffer);
            if (buffer.contains("self")) {
                uploadSucc = true;
            }

        }
        outStream.flush();
        outStream.close();
        socket.close();
        return uploadSucc;
    }

    private static StringBuilder buildFileHeader(String BOUNDARY, File fp) throws IOException {
        StringBuilder httpFile = new StringBuilder();

        httpFile.append("--")
                .append(BOUNDARY)
                .append("\r\n")
                .append("Content-Disposition: form-data;name=\"file\";filename=\"").append(fp.getName()).append("\"\r\n");
        String type = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(getExtension(fp));
        httpFile.append("Content-Type: ").append(type).append("\r\n\r\n");
        return httpFile;
    }

    private static String getExtension(final File file) {
        String suffix = "";
        String name = file.getName();
        final int idx = name.lastIndexOf(".");
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }

}
