package com.fox.stockhelper.database.dao;

import android.content.Context;

import com.fox.stockhelper.database.OrmDatabaseHelper;

/**
 * 数据库操作基类
 *
 * @author lusongsong
 * @date 2020/11/24 22:07
 */
public class BaseDao {
    /**
     * 操作助手
     */
    protected OrmDatabaseHelper ormDatabaseHelper = null;

    /**
     * 构造函数
     */
    public BaseDao(Context context) {
        ormDatabaseHelper = new OrmDatabaseHelper(context);
    }
}
