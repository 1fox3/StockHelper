package com.fox.stockhelper.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

/**
 * 股市交易状态
 *
 * @author lusongsong
 * @date 2020/12/2 14:47
 */
@DatabaseTable(tableName = "t_deal_status")
@Data
public class StockMarketDealStatusBean {
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
     * 交易状态
     */
    @DatabaseField(columnName = "dealStatus")
    Integer dealStatus;
}
