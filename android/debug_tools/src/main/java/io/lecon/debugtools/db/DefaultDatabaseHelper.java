package io.lecon.debugtools.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.lecon.debugtools.Constants;

import static io.lecon.debugtools.DebugTools.TAG;

/**
 * Created by spawn on 17-9-28.
 */

public class DefaultDatabaseHelper implements DatabaseHelper {

    private Context mContext;

    public DefaultDatabaseHelper(Context context) {
        this.mContext = context;
    }

    @Override
    public HashMap<String, File> listAllDatabase() {
        HashMap<String, File> databaseFiles = new HashMap<>();
        try {
            for (String databaseName : mContext.databaseList()) {
                if (!databaseName.contains("journal")) {
                    databaseFiles.put(databaseName, mContext.getDatabasePath(databaseName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseFiles;
    }

    @Override
    public List<String> listAllTables(String dbName) throws SQLException {
        File dbFile = listAllDatabase().get(dbName);
        if (dbFile == null) {
            throw new IllegalArgumentException("Cannot find dbName :" + dbName);
        }
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsolutePath(), null);
        List<String> tableName = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' OR type='view'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String item = c.getString(0);
                if (!"android_metadata".equals(item) && !"transaction".equals(item)) {
                    tableName.add(item);
                }
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return tableName;
    }

    public static class TableInfo {
        public String columnName;
        public boolean isPrimary;
    }

    /**
     * 获取表信息
     */
    private static List<TableInfo> getTableInfo(SQLiteDatabase db, String pragmaQuery) {
        Cursor cursor;
        try {
            cursor = db.rawQuery(pragmaQuery, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (cursor != null) {
            List<TableInfo> tableInfoList = new ArrayList<>();
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    TableInfo tableInfo = new TableInfo();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        final String columnName = cursor.getColumnName(i);

                        switch (columnName) {
                            case Constants.PK:
                                tableInfo.isPrimary = cursor.getInt(i) == 1;
                                break;
                            case Constants.NAME:
                                tableInfo.columnName = cursor.getString(i);
                                break;
                            default:
                        }
                    }
                    tableInfoList.add(tableInfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return tableInfoList;
        }
        return null;
    }

    @Override
    public String queryData(String dbName, String tableName, Map<String, Object> condition, String limit, String offset) {
        File dbFile = listAllDatabase().get(dbName);
        if (dbFile == null) {
            throw new IllegalArgumentException("Cannot find dbName :" + dbName);
        }
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsolutePath(), null);

        StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName);
        List<String> ion = new ArrayList<>();
        if (condition != null) {
            sql.append(" WHERE ");
            Set<String> keySet = condition.keySet();
            Iterator<String> iterator = keySet.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                i++;
                String con = iterator.next();
                if (con.contains("=")) {
                    return null;
                }
                sql.append(con);
                sql.append("=?");
                ion.add(String.valueOf(condition.get(con)));
                if (i != keySet.size()) {
                    sql.append(" and ");
                }
            }
        }
        try {
            int l = Integer.parseInt(limit);
            if (l != 0) {
                sql.append(" LIMIT ").append(l);
            }
            int o = Integer.parseInt(offset);
            if (o != 0) {
                sql.append(" OFFSET ").append(o);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "执行SQL: " + sql.toString());
        Cursor cursor = db.rawQuery(sql.toString(), ion.toArray(new String[ion.size()]));
        JSONObject respObj = new JSONObject();
        JSONArray jsonList = new JSONArray();
        try {
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject();
                    for (int j = 0; j < cursor.getColumnCount(); j++) {
                        switch (cursor.getType(j)) {
                            case Cursor.FIELD_TYPE_BLOB:
                                jsonObject.put(cursor.getColumnName(j),
                                        cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(j))) == 1);
                                break;
                            case Cursor.FIELD_TYPE_FLOAT:
                                jsonObject.put(cursor.getColumnName(j),
                                        cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(j))));
                                break;
                            case Cursor.FIELD_TYPE_INTEGER:
                                jsonObject.put(cursor.getColumnName(j),
                                        cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(j))));
                                break;
                            case Cursor.FIELD_TYPE_STRING:
                            default:
                                jsonObject.put(cursor.getColumnName(j),
                                        cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j))));
                        }
                    }
                    jsonList.put(jsonObject);
                } while (cursor.moveToNext());
            }
            if (tableName != null) {
                // 获取主键
                final String pragmaQuery = "PRAGMA table_info(" + tableName + ")";
                List<TableInfo> tableInfos = getTableInfo(db, pragmaQuery);
                if (tableInfos != null) {
                    JSONArray jsonArray = new JSONArray();
                    for (TableInfo tableInfo : tableInfos) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("columnName", tableInfo.columnName);
                        jsonObject.put("isPrimary", tableInfo.isPrimary);
                        jsonArray.put(jsonObject);
                    }
                    respObj.put("columns", jsonArray);
                }
            }
            respObj.put("list", jsonList);
            respObj.put("pageInfo", countTableData(dbName, tableName));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        cursor.close();
        return respObj.toString();
    }

    @Override
    public JSONObject countTableData(String dbName, String tableName) {
        File dbFile = listAllDatabase().get(dbName);
        if (dbFile == null) {
            throw new IllegalArgumentException("Cannot find dbName :" + dbName);
        }
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsolutePath(), null);
        StringBuilder sql = new StringBuilder("SELECT count(*) FROM " + tableName);
        Log.i(TAG, "执行SQL: " + sql.toString());
        Cursor cursor = db.rawQuery(sql.toString(), new String[]{});
        JSONObject jsonObject = new JSONObject();
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            try {
                jsonObject.put("count", count);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return jsonObject;
    }

    @Override
    public String countData(String dbName, String tableName, Map<String, Object> where) {
        String data = queryData(dbName, tableName, where);
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray(data);
            int count = jsonArray.length();
            jsonObject.put("count", count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public int version(String dbName) {
        int version = -1;
        File dbFile = listAllDatabase().get(dbName);
        if (dbFile == null) {
            throw new IllegalArgumentException("Cannot find dbName :" + dbName);
        }
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsolutePath(), null);
        version = db.getVersion();
        db.close();
        return version;
    }

    @Override
    public String queryData(String dbName, String tableName, Map<String, Object> condition) throws SQLException {
        return queryData(dbName, tableName, condition, "0", "0");
    }

    @Override
    public void updateData(String dbName, String tableName, Map<String, Object> condition, Map<String, Object> newValue) throws SQLException {
        File dbFile = listAllDatabase().get(dbName);
        if (dbFile == null) {
            throw new IllegalArgumentException("Cannot find dbName :" + dbName);
        }
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsolutePath(), null);
        StringBuilder sql = new StringBuilder("UPDATE " + tableName);
        List<Object> ion = new ArrayList<>();
        if (newValue != null) {
            sql.append(" SET ");
            Set<String> keySet = newValue.keySet();
            Iterator<String> iterator = keySet.iterator();
            int j = 0;
            while (iterator.hasNext()) {
                j++;
                String key = iterator.next();
                if (key.contains("=")) {
                    throw new SQLException("error params,must be not contain '='");
                }
                sql.append(key);
                sql.append("=?");
                ion.add(newValue.get(key));
                if (j != keySet.size()) {
                    sql.append(" , ");
                }
            }
        }

        if (condition != null) {
            sql.append(" WHERE ");
            Set<String> keySet = condition.keySet();
            Iterator<String> iterator = keySet.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                i++;
                String con = iterator.next();
                if (con.contains("=")) {
                    throw new SQLException("error params,must be not contain '='");
                }
                sql.append(con);
                sql.append("=?");
                ion.add(condition.get(con));
                if (i != keySet.size()) {
                    sql.append(" and ");
                }
            }
        }
        Log.i(TAG, "执行SQL: " + sql.toString());
        db.execSQL(sql.toString(), ion.toArray(new Object[ion.size()]));
    }

    @Override
    public void insertData(String dbName, String tableName, Map<String, Object> newValue) throws SQLException {
        File dbFile = listAllDatabase().get(dbName);
        if (dbFile == null) {
            throw new IllegalArgumentException("Cannot find dbName :" + dbName);
        }
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsolutePath(), null);
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName);
        List<Object> ion = new ArrayList<>();
        if (newValue != null) {
            sql.append(" (");
            Set<String> keySet = newValue.keySet();
            Iterator<String> iterator = keySet.iterator();
            int j = 0;
            while (iterator.hasNext()) {
                j++;
                String key = iterator.next();
                sql.append(key);
                ion.add(newValue.get(key));
                if (j != keySet.size()) {
                    sql.append(",");
                }
            }
            sql.append(") ");
            sql.append("VALUES");
            sql.append(" (");
            for (int k = 0; k < newValue.size(); k++) {
                sql.append('?');
                if (k != newValue.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }
        Log.i(TAG, "执行SQL: " + sql.toString());
        db.execSQL(sql.toString(), ion.toArray(new Object[ion.size()]));
    }

    @Override
    public void deleteData(String dbName, String tableName, Map<String, Object> condition) throws SQLException {
        File dbFile = listAllDatabase().get(dbName);
        if (dbFile == null) {
            throw new IllegalArgumentException("Cannot find dbName :" + dbName);
        }
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsolutePath(), null);
        StringBuilder sql = new StringBuilder("DELETE FROM " + tableName);
        List<Object> ion = new ArrayList<>();
        if (condition != null) {
            sql.append(" WHERE ");
            Set<String> keySet = condition.keySet();
            Iterator<String> iterator = keySet.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                i++;
                String con = iterator.next();
                if (con.contains("=")) {
                    throw new SQLException("error params,must be not contain '='");
                }
                sql.append(con);
                sql.append("=?");
                ion.add(condition.get(con));
                if (i != keySet.size()) {
                    sql.append(" and ");
                }
            }
        }
        Log.i(TAG, "执行SQL: " + sql.toString());
        db.execSQL(sql.toString(), ion.toArray(new Object[ion.size()]));
    }

    @Override
    public void sql(String dbName, String sql) throws SQLException {
        File dbFile = listAllDatabase().get(dbName);
        if (dbFile == null) {
            throw new IllegalArgumentException("Cannot find dbName :" + dbName);
        }
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsolutePath(), null);
        Log.i(TAG, "执行SQL: " + sql);
        db.execSQL(sql);
    }
}
