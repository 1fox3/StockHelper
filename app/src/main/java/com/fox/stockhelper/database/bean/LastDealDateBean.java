package com.fox.stockhelper.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

/**
 * 股市最新交易日
 *
 * @author lusongsong
 * @date 2020/11/24 20:40
 */
@DatabaseTable(tableName = "t_last_deal_Date")
@Data
public class LastDealDateBean {
    @DatabaseField(generatedId = true, columnName = "id")
    Integer id;
    @DatabaseField(columnName = "stockMarket")
    Integer stockMarket;
    @DatabaseField(columnName = "dealDate")
    String dealDate;
}
