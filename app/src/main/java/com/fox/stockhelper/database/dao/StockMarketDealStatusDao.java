package com.fox.stockhelper.database.dao;

import android.content.Context;

import com.fox.stockhelper.database.bean.StockMarketDealStatusBean;
import com.fox.stockhelper.util.LogUtil;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * 股市交易状态数据库操作类
 *
 * @author lusongsong
 * @date 2020/12/2 14:49
 */
public class StockMarketDealStatusDao extends BaseDao {
    /**
     * 数据库操作类
     */
    private Dao<StockMarketDealStatusBean, Integer> dao;

    /**
     * 构造函数
     *
     * @param context
     */
    public StockMarketDealStatusDao(Context context) {
        super(context);
        dao = ormDatabaseHelper.getDao(StockMarketDealStatusBean.class);
    }

    /**
     * 更新状态
     *
     * @param stockMarket
     * @return
     */
    public int updateStatus(Integer stockMarket, Integer dealStatus) {
        try {
            if (null == stockMarket || null == dealStatus) {
                return 0;
            }
            StockMarketDealStatusBean stockMarketDealStatusBean =
                    new StockMarketDealStatusBean();
            stockMarketDealStatusBean.setStockMarket(stockMarket);
            stockMarketDealStatusBean.setDealStatus(dealStatus);
            List<StockMarketDealStatusBean> stockMarketDealStatusBeanList =
                    dao.queryBuilder().where()
                            .eq("stockMarket", stockMarket).query();
            if (null == stockMarketDealStatusBeanList
                    || stockMarketDealStatusBeanList.isEmpty()) {
                return dao.create(stockMarketDealStatusBean);
            } else {
                for (int i = 0; i < stockMarketDealStatusBeanList.size(); i++) {
                    if (0 != i) {
                        dao.delete(stockMarketDealStatusBeanList.get(i));
                    }
                    StockMarketDealStatusBean dbStockMarketDealStatusBean =
                            stockMarketDealStatusBeanList.get(i);
                    stockMarketDealStatusBean.setId(dbStockMarketDealStatusBean.getId());
                    dao.update(stockMarketDealStatusBean);
                }
                return stockMarketDealStatusBean.getId();
            }
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
        }
        return 0;
    }

    /**
     * 获取交易状态
     *
     * @param stockMarket
     * @return
     */
    public Integer dealStatus(Integer stockMarket) {
        try {
            StockMarketDealStatusBean stockMarketDealStatusBean  =
                    dao.queryBuilder().where()
                            .eq("stockMarket", stockMarket).queryForFirst();
            return null == stockMarketDealStatusBean ?
                    null : stockMarketDealStatusBean.getDealStatus();
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
        }
        return null;
    }


}
