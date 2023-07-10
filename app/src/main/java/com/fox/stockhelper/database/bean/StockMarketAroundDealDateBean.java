package com.fox.stockhelper.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 股市交易日
 *
 * @author lusongsong
 * @date 2020/11/24 20:40
 */
@DatabaseTable(tableName = "t_deal_Date")
public class StockMarketAroundDealDateBean {
    /**
     * 记录id
     */
    @DatabaseField(generatedId = true, columnName = "id")
    Integer id;
    /**
     * 股市id
     */
    @DatabaseField(columnName = "stockMarket")
    Integer stockMarket;
    /**
     * 日期类型
     */
    @DatabaseField(columnName = "type")
    String type;
    /**
     * 日期
     */
    @DatabaseField(columnName = "dealDate")
    String dealDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStockMarket() {
        return stockMarket;
    }

    public void setStockMarket(Integer stockMarket) {
        this.stockMarket = stockMarket;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }
}
