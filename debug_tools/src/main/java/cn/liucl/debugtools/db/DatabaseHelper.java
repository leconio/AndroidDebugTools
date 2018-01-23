package cn.liucl.debugtools.db;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spawn on 17-9-28.
 */

public interface DatabaseHelper {

    /**
     * 列出所有数据库
     *
     * @return
     */
    HashMap<String, File> listAllDatabase();

    /**
     * 列出数据库所有表名
     *
     * @param dbName 数据库名
     * @return
     */
    List<String> listAllTables(String dbName);

    /**
     * 查询数据
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @param where     查询条件
     * @return 返回结果 json格式
     */
    String queryData(String dbName, String tableName, Map<String, String> where);

    /**
     * 更新
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @param where     查询条件
     * @param newValue  新值
     * @return 返回结果 json格式
     */
    void updateData(String dbName, String tableName, Map<String, String> where, Map<String, String> newValue);

    /**
     * 插入一条数据
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @param newValue  新值
     * @return 返回结果 json格式
     */
    void insertData(String dbName, String tableName,Map<String, String> newValue);

    /**
     * 删除一条数据
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @param where     查询条件
     * @return 返回结果 json格式
     */
    void deleteData(String dbName, String tableName, Map<String, String> where);
}