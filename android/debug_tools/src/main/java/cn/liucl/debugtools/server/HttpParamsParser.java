package cn.liucl.debugtools.server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by spawn on 17-9-28.
 */

public class HttpParamsParser {

    /**
     * 分析url字符串,采用utf-8解码
     *
     * @param body
     * @return
     */
    public static Request parse(String body) {
        return parse(body, "utf-8");
    }

    /**
     * 分析url字符串,指定字符集进行解码
     *
     * @param body
     * @param enc
     * @return
     */
    public static Request parse(String body, String enc) {
        Request request = null;
        if (body == null || body.length() == 0) {
            return new Request();
        }
        String urlBody = body.split("\r\n")[0].split(" ")[1];
        int questIndex = urlBody.indexOf('?');
        if (questIndex == -1) {
            return new Request(urlBody);
        }
        String url = urlBody.substring(0, questIndex);
        String queryString = urlBody.substring(questIndex + 1, urlBody.length());
        //POST 仅支持JSON方式
        if (body.startsWith("POST")) {
            String[] split = body.split("\r\n\r\n");
            if (split.length > 1) {
                String bodyEntry = split[1];
                try {
                    JSONObject jsonObject = new JSONObject(bodyEntry);
                    Iterator<String> keys = jsonObject.keys();
                    Map<String, Object> paramsMap = new HashMap<>();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        paramsMap.put(key, jsonObject.get(key));
                    }
                    request = new Request(url, getParamsMap(queryString, enc), paramsMap);
                    request.setMethod("POST");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (body.startsWith("GET")) {
            request = new Request(url, getParamsMap(queryString, enc));
            request.setMethod("GET");
        } else if(body.startsWith("OPTIONS")) {
            request = new Request();
            request.setMethod("OPTIONS");
        } else {
            request = new Request("/error");
            request.setParameter("msg","Unsupported Method");
        }
        return request;
    }


    private static Map<String, String[]> getParamsMap(String queryString, String enc) {
        Map<String, String[]> paramsMap = new HashMap<String, String[]>();
        if (queryString != null && queryString.length() > 0) {
            int ampersandIndex, lastAmpersandIndex = 0;
            String subStr, param, value;
            String[] paramPair, values, newValues;
            do {
                ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
                if (ampersandIndex > 0) {
                    subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
                    lastAmpersandIndex = ampersandIndex;
                } else {
                    subStr = queryString.substring(lastAmpersandIndex);
                }
                paramPair = subStr.split("=");
                param = paramPair[0];
                value = paramPair.length == 1 ? "" : paramPair[1];
                try {
                    value = URLDecoder.decode(value, enc);
                } catch (UnsupportedEncodingException ignored) {
                }
                if (paramsMap.containsKey(param)) {
                    values = paramsMap.get(param);
                    int len = values.length;
                    newValues = new String[len + 1];
                    System.arraycopy(values, 0, newValues, 0, len);
                    newValues[len] = value;
                } else {
                    newValues = new String[]{value};
                }
                paramsMap.put(param, newValues);
            } while (ampersandIndex > 0);
        }
        return paramsMap;
    }

    /**
     * 请求对象
     *
     * @author yy
     * @date Jun 21, 2009 2:17:31 PM
     */
    public static class Request {
        private String method;
        private String requestURI;
        private Map<String, String[]> parameterMap;
        private Map<String, Object> bodyParamterMap;

        public Request() {
            this("");
        }

        public Request(String requestURI) {
            this.requestURI = requestURI;
            parameterMap = new HashMap<String, String[]>();
        }

        public Request(String requestURI, Map<String, String[]> parameterMap) {
            this.requestURI = requestURI;
            this.parameterMap = parameterMap;
        }

        public Request(String requestURI, Map<String, String[]> parameterMap, Map<String, Object> bodyParamterMap) {
            this.requestURI = requestURI;
            this.parameterMap = parameterMap;
            this.bodyParamterMap = bodyParamterMap;
        }

        public void setParameter(String name,String msg) {
            if (parameterMap!=null) {
                parameterMap.put(name, new String[]{msg});
            }
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        /**
         * 获得指定名称的参数
         *
         * @param name
         * @return
         */
        public String getGetParameter(String name) {
            String[] values = parameterMap.get(name);
            if (values != null && values.length > 0) {
                return values[0];
            }
            return null;
        }

        /**
         * 获得指定名称的参数
         *
         * @param name
         * @return
         */
        public Object getPostParameter(String name) {
            return bodyParamterMap.get(name);
        }


        /**
         * 获得所有的参数名称
         *
         * @return
         */
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(parameterMap.keySet());
        }

        /**
         * 获得指定名称的参数值(多个)
         *
         * @param name
         * @return
         */
        public String[] getParameterValues(String name) {
            return parameterMap.get(name);
        }

        /**
         * 获得请求的url地址
         *
         * @return
         */
        public String getRequestURI() {
            return requestURI;
        }

        /**
         * 获得 参数-值Map
         *
         * @return
         */
        public Map<String, String[]> getParameterMap() {
            return parameterMap;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append("{");
            buf.append("\n  url = ").append(this.requestURI);
            buf.append("\n  paramsMap = {");
            if (this.parameterMap.size() > 0) {
                for (Map.Entry<String, String[]> e : this.parameterMap.entrySet()) {
                    buf.append(e.getKey()).append("=").append(Arrays.toString(e.getValue())).append(",");
                }
                buf.deleteCharAt(buf.length() - 1);
            }
            buf.append("}\n}");
            return buf.toString();
        }
    }
}
