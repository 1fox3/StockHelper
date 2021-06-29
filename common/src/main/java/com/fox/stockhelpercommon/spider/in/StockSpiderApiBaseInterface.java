package com.fox.stockhelpercommon.spider.in;

/**
 * 股票爬虫基本接口
 *
 * @author lusongsong
 * @date 2021/1/26 11:09
 */
public interface StockSpiderApiBaseInterface {
    /**
     * 是否支持该证券所
     *
     * @param stockMarket
     * @return
     */
    boolean isSupport(int stockMarket);

    /**
     * 权重
     *
     * @return
     */
    int weight();
}
