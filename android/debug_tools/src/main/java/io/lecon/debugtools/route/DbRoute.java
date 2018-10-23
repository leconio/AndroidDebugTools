package io.lecon.debugtools.route;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.lecon.debugtools.db.DefaultDatabaseHelper;
import io.lecon.debugtools.server.HttpParamsParser;
import io.lecon.debugtools.server.Result;

/**
 * Created by spawn on 17-9-28.
 */

public class DbRoute implements Route {

    private Context mContext;

    public DbRoute(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Result process(HttpParamsParser.Request request) {
        DefaultDatabaseHelper helper = new DefaultDatabaseHelper(mContext);
        String action = request.getRequestURI().split("/")[2];
        Result result = new Result();
        try {
            result.setMessage("ok");
            result.setSuccessful(true);
            switch (action) {
                case "update":
                    String dbName = request.getGetParameter("dbName");
                    String tableName = request.getGetParameter("tableName");
                    String condition = (String) request.getPostParameter("condition");
                    String newValue = (String) request.getPostParameter("newValue");
                    helper.updateData(dbName, tableName, buildParamMap(condition), buildParamMap(newValue));
                    break;
                case "insert":
                    newValue = (String) request.getPostParameter("newValue");
                    dbName = request.getGetParameter("dbName");
                    tableName = request.getGetParameter("tableName");
                    helper.insertData(dbName, tableName, buildParamMap(newValue));
                    break;
                case "query":
                    condition = request.getGetParameter("condition");
                    tableName = request.getGetParameter("tableName");
                    dbName = request.getGetParameter("dbName");
                    String limit = request.getGetParameter("limit");
                    String offset = request.getGetParameter("offset");
                    String jsonData = helper.queryData(dbName, tableName, buildParamMap(condition), limit, offset);
                    result.setObj(jsonData);
                    break;
                case "count":
                    condition = request.getGetParameter("condition");
                    tableName = request.getGetParameter("tableName");
                    dbName = request.getGetParameter("dbName");
                    jsonData = helper.countData(dbName, tableName, buildParamMap(condition));
                    result.setObj(jsonData);
                    break;
                case "delete":
                    condition = request.getGetParameter("condition");
                    tableName = request.getGetParameter("tableName");
                    dbName = request.getGetParameter("dbName");
                    helper.deleteData(dbName, tableName, buildParamMap(condition));
                    break;
                case "listTable":
                    dbName = request.getGetParameter("dbName");
                    List<String> tables = helper.listAllTables(dbName);
                    JSONArray jsonArray = new JSONArray();
                    for (String table : tables) {
                        jsonArray.put(table);
                    }
                    result.setObj(jsonArray.toString());
                    break;
                case "listDatabase":
                    HashMap<String, File> databases = helper.listAllDatabase();
                    Iterator<String> iterator = databases.keySet().iterator();
                    jsonArray = new JSONArray();
                    while (iterator.hasNext()) {
                        JSONObject object = new JSONObject();
                        String next = iterator.next();
                        object.put("name", next);
                        object.put("path", databases.get(next).getPath());
                        jsonArray.put(object);
                    }
                    result.setObj(jsonArray.toString());
                    break;
                case "sql":
                    dbName = request.getGetParameter("dbName");
                    String sql = request.getGetParameter("sql");
                    helper.sql(dbName, sql);
                    break;
                case "version":
                    dbName = request.getGetParameter("dbName");
                    int version = helper.version(dbName);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("version", version);
                    result.setObj(jsonObject.toString());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.toString());
            result.setSuccessful(false);
        }
        return result;
    }

    /**
     * 把字符串分割成map
     */
    private Map<String, String> buildParamMap(String condition) {
        if (TextUtils.isEmpty(condition) || "null".equals(condition)) {
            return null;
        }
        Map<String, String> condMap = new HashMap<>();
        // 字段与字段
        String[] conditionOne = condition.split("驫羴");
        for (String cond : conditionOne) {
            // 字段内
            String[] split = cond.split("靐龘");
            String key = split[0];
            String val = "";
            if (split.length != 1) {
                val = split[1];
            }
            condMap.put(key, val);

        }
        return condMap;
    }

}
