package com.fox.stockhelper.constant.stock;

import com.fox.spider.stock.constant.StockMarketStatusConst;
import com.fox.stockhelper.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 股市状态相关常量
 *
 * @author lusongsong
 * @date 2020/8/20 10:20
 */
public class SHStockConst {
    /**
     * 交易状态图片对应
     */
    public static final Map<Integer, Integer> STATUS_IMAGE_MAP = new HashMap<Integer, Integer>() {{
        put(StockMarketStatusConst.UNKNOWN, R.drawable.sm_status_unknown);
        put(StockMarketStatusConst.OPEN, R.drawable.sm_status_open);
        put(StockMarketStatusConst.CLOSE, R.drawable.sm_status_close);
        put(StockMarketStatusConst.REST, R.drawable.sm_status_open);
        put(StockMarketStatusConst.COMPETE, R.drawable.sm_status_compete);
        put(StockMarketStatusConst.INIT, R.drawable.sm_status_init);
        put(StockMarketStatusConst.NOON, R.drawable.sm_status_noon);
        put(StockMarketStatusConst.SOON, R.drawable.sm_status_soon);
    }};
}
