package com.fox.stockhelper.database.dao;

import android.content.Context;

import com.fox.stockhelper.database.bean.StockMarketAroundDealDateBean;
import com.fox.stockhelper.util.LogUtil;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * 股市交替日期数据库操作类
 *
 * @author lusongsong
 * @date 2020/11/24 22:07
 */
public class StockMarketAroundDealDateDao extends BaseDao {
    /**
     * 上个交易日
     */
    public static String TYPE_PRE = "pre";
    /**
     * 最新交易日
     */
    public static String TYPE_LAST = "last";
    /**
     * 下个交易日
     */
    public static String TYPE_NEXT = "next";
    /**
     * 数据库操作类
     */
    private Dao<StockMarketAroundDealDateBean, Integer> dao;

    /**
     * 构造函数
     */
    public StockMarketAroundDealDateDao(Context context) {
        super(context);
        dao = ormDatabaseHelper.getDao(StockMarketAroundDealDateBean.class);
    }

    /**
     * 获取交易日期
     *
     * @param stockMarket
     * @return
     */
    public StockMarketAroundDealDateBean getDealDateByType(Integer stockMarket, String type) {
        try {
            StockMarketAroundDealDateBean stockMarketAroundDealDateBean =
                    dao.queryBuilder().where()
                            .eq("stockMarket", stockMarket).and()
                            .eq("type", type).queryForFirst();
            return stockMarketAroundDealDateBean;
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取交易日期
     *
     * @param stockMarket
     * @return
     */
    public int saveDealDateByType(Integer stockMarket, String type, String dealDate) {
        try {
            if (null == stockMarket || null == type || type.isEmpty()
                    || null == dealDate || dealDate.isEmpty()) {
                return 0;
            }
            StockMarketAroundDealDateBean stockMarketAroundDealDateBean =
                    new StockMarketAroundDealDateBean();
            stockMarketAroundDealDateBean.setStockMarket(stockMarket);
            stockMarketAroundDealDateBean.setType(type);
            List<StockMarketAroundDealDateBean> stockMarketAroundDealDateBeanList =
                    dao.queryBuilder().where()
                            .eq("stockMarket", stockMarket).and()
                            .eq("type", type).query();
            stockMarketAroundDealDateBean.setDealDate(dealDate);
            if (null == stockMarketAroundDealDateBeanList
                    || stockMarketAroundDealDateBeanList.isEmpty()) {
                return dao.create(stockMarketAroundDealDateBean);
            } else {
                for (int i = 0; i < stockMarketAroundDealDateBeanList.size(); i++) {
                    if (0 != i) {
                        dao.delete(stockMarketAroundDealDateBeanList.get(i));
                    }
                    StockMarketAroundDealDateBean dbStockMarketAroundDealDateBean = stockMarketAroundDealDateBeanList.get(i);
                    stockMarketAroundDealDateBean.setId(dbStockMarketAroundDealDateBean.getId());
                    dao.update(stockMarketAroundDealDateBean);
                }
                return stockMarketAroundDealDateBean.getId();
            }
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
        }
        return 0;
    }

    /**
     * 获取上个交易日
     *
     * @param stockMarket
     * @return
     */
    public StockMarketAroundDealDateBean pre(Integer stockMarket) {
        return getDealDateByType(stockMarket, TYPE_PRE);
    }

    /**
     * 获取最新交易日
     *
     * @param stockMarket
     * @return
     */
    public StockMarketAroundDealDateBean last(Integer stockMarket) {
        return getDealDateByType(stockMarket, TYPE_LAST);
    }

    /**
     * 获取下个交易日
     *
     * @param stockMarket
     * @return
     */
    public StockMarketAroundDealDateBean next(Integer stockMarket) {
        return getDealDateByType(stockMarket, TYPE_NEXT);
    }

    /**
     * 保存上一个交易日
     *
     * @param stockMarket
     * @param dealDate
     * @return
     */
    public int savePre(Integer stockMarket, String dealDate) {
        return saveDealDateByType(stockMarket, TYPE_PRE, dealDate);
    }

    /**
     * 保存最新交易日
     *
     * @param stockMarket
     * @param dealDate
     * @return
     */
    public int saveLast(Integer stockMarket, String dealDate) {
        return saveDealDateByType(stockMarket, TYPE_LAST, dealDate);
    }

    /**
     * 保存下一个交易日
     *
     * @param stockMarket
     * @param dealDate
     * @return
     */
    public int saveNext(Integer stockMarket, String dealDate) {
        return saveDealDateByType(stockMarket, TYPE_NEXT, dealDate);
    }
}
