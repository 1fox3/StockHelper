package com.fox.stockhelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fox.stockhelper.database.bean.LastDealDateBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Synchronized;

/**
 * @author lusongsong
 * @date 2020/11/24 22:08
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    /**
     * 数据库名称
     */
    private static final String DATABASE_NAME = "stockhelper.db";
    /**
     * 数据库版本
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * 数据表实体类列表
     */
    private static List<Class> tables = Arrays.asList(
            LastDealDateBean.class
    );
    /**
     * 上下文
     */
    private Context myContext;
    /**
     * DAO类映射，用于操控不同的表
     */
    private static Map<String, Dao> tableDao = new HashMap<>();

    /**
     * 构造类
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    /**
     * 创建表
     *
     * @param sqLiteDatabase
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            if (null == tables || tables.isEmpty()) {
                return;
            }
            for (Class tableClass : tables) {
                TableUtils.createTable(connectionSource, tableClass);
            }
        } catch (SQLException e) {
            Log.e("dbOnCreate", e.getMessage());
        }
    }

    /**
     * 更新表
     *
     * @param sqLiteDatabase
     * @param connectionSource
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            if (null == tables || tables.isEmpty()) {
                return;
            }
            for (Class tableClass : tables) {
                TableUtils.dropTable(connectionSource, tableClass, true);
            }
        } catch (SQLException e) {
            Log.e("dbOnUpgrade", e.getMessage());
        }
    }

    /**
     * 获取数据操作类
     *
     * @param tableClass
     * @return
     */
    @Synchronized
    public Dao getDao(Class tableClass) {
        String className = tableClass.getName();
        if (!tableDao.containsKey(className)) {
            try {
                Dao dao = super.getDao(tableClass);
                tableDao.put(className, dao);
            } catch (SQLException e) {
                Log.e("dbGetDao", e.getMessage());
            }
        }
        return tableDao.containsKey(className) ? tableDao.get(className) : null;
    }

}
