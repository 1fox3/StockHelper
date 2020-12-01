package com.fox.stockhelper.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

/**
 * 股市交易日
 *
 * @author lusongsong
 * @date 2020/11/24 20:40
 */
@DatabaseTable(tableName = "t_deal_Date")
@Data
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
}
