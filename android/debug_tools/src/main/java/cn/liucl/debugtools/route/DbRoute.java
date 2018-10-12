package cn.liucl.debugtools.route;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.liucl.debugtools.db.DefaultDatabaseHelper;
import cn.liucl.debugtools.server.HttpParamsParser;
import cn.liucl.debugtools.server.Result;

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
                    String dbName = request.getParameter("dbName");
                    String tableName = request.getParameter("tableName");
                    String condition = request.getParameter("condition");
                    String newValue = request.getParameter("newValue");
                    helper.updateData(dbName, tableName, buildParamMap(condition), buildParamMap(newValue));
                    break;
                case "insert":
                    newValue = request.getParameter("newValue");
                    dbName = request.getParameter("dbName");
                    tableName = request.getParameter("tableName");
                    helper.insertData(dbName, tableName, buildParamMap(newValue));
                    break;
                case "query":
                    condition = request.getParameter("condition");
                    tableName = request.getParameter("tableName");
                    dbName = request.getParameter("dbName");
                    String limit = request.getParameter("limit");
                    String offset = request.getParameter("offset");
                    String jsonData = helper.queryData(dbName, tableName, buildParamMap(condition), limit, offset);
                    result.setObj(jsonData);
                    break;
                case "count":
                    condition = request.getParameter("condition");
                    tableName = request.getParameter("tableName");
                    dbName = request.getParameter("dbName");
                    jsonData = helper.countData(dbName, tableName, buildParamMap(condition));
                    result.setObj(jsonData);
                    break;
                case "delete":
                    condition = request.getParameter("condition");
                    tableName = request.getParameter("tableName");
                    dbName = request.getParameter("dbName");
                    helper.deleteData(dbName, tableName, buildParamMap(condition));
                    break;
                case "listTable":
                    dbName = request.getParameter("dbName");
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
                    dbName = request.getParameter("dbName");
                    String sql = request.getParameter("sql");
                    helper.sql(dbName, sql);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccessful(false);
        }
        return result;
    }

    /**
     * 把字符串分割成map
     */
    private Map<String, String> buildParamMap(String condition) {
        if (TextUtils.isEmpty(condition)) {
            return null;
        }
        Map<String, String> condMap = new HashMap<>();
        String[] conditionOne = condition.split(";");
        for (String cond : conditionOne) {
            String[] split = cond.split(":");
            String key = split[0];
            String val = split[1];
            condMap.put(key, val);
        }
        return condMap;
    }

}
