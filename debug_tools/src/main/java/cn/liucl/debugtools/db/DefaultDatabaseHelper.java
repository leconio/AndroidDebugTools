package cn.liucl.debugtools.db;

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

import static cn.liucl.debugtools.DebugTools.TAG;

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
                databaseFiles.put(databaseName, mContext.getDatabasePath(databaseName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseFiles;
    }

    @Override
    public List<String> listAllTables(String dbName) {
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(listAllDatabase().get(dbName).getAbsolutePath(), null);
        List<String> tableName = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' OR type='view'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                tableName.add(c.getString(0));
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return tableName;
    }

    @Override
    public String queryData(String dbName, String tableName, Map<String, String> condition) {
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(listAllDatabase().get(dbName).getAbsolutePath(), null);
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
                ion.add(condition.get(con));
                if (i != keySet.size()) {
                    sql.append(" and ");
                }
            }
        }
        Log.i(TAG, "执行SQL: " + sql.toString());
        Cursor cursor = db.rawQuery(sql.toString(), ion.toArray(new String[ion.size()]));
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
                                        cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(j))));
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        cursor.close();
        return jsonList.toString();
    }

    @Override
    public void updateData(String dbName, String tableName, Map<String, String> condition, Map<String, String> newValue) {
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(listAllDatabase().get(dbName).getAbsolutePath(), null);
        StringBuilder sql = new StringBuilder("UPDATE " + tableName);
        List<String> ion = new ArrayList<>();
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
        db.execSQL(sql.toString(), ion.toArray(new String[ion.size()]));
    }

    @Override
    public void insertData(String dbName, String tableName, Map<String, String> newValue) {
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(listAllDatabase().get(dbName).getAbsolutePath(), null);
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName);
        List<String> ion = new ArrayList<>();
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
        db.execSQL(sql.toString(), ion.toArray(new String[ion.size()]));
    }

    @Override
    public void deleteData(String dbName, String tableName, Map<String, String> condition) {
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(listAllDatabase().get(dbName).getAbsolutePath(), null);
        StringBuilder sql = new StringBuilder("DELETE FROM " + tableName);
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
        db.execSQL(sql.toString(), ion.toArray(new String[ion.size()]));
    }

}
