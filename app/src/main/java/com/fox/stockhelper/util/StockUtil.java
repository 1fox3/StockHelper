package com.fox.stockhelper.util;

import com.fox.spider.stock.constant.StockConst;
import com.fox.stockhelper.R;

import java.math.BigDecimal;

/**
 * 股市工具类
 *
 * @author lusongsong
 * @date 2020/8/20 15:06
 */
public class StockUtil {
    /**
     * 获取增长类型
     *
     * @param price
     * @return
     */
    public static int getUptickType(BigDecimal price, BigDecimal preClosePrice) {
        int type = StockConst.UPTICK_TYPE_FLAT;
        if (null != price && null != preClosePrice &&
                0 < preClosePrice.compareTo(BigDecimal.ZERO)
                && 0 < price.compareTo(BigDecimal.ZERO)) {
            int com = price.compareTo(preClosePrice);
            if (0 < com) {
                type = StockConst.UPTICK_TYPE_UP;
            } else if (0 > com) {
                type = StockConst.UPTICK_TYPE_DOWN;
            } else {
                type = StockConst.UPTICK_TYPE_FLAT;
            }
        }
        return type;
    }

    /**
     * 获取不同增幅类型的颜色
     *
     * @param uptickType
     * @return
     */
    public static int getColor(int uptickType) {
        int color;
        switch (uptickType) {
            case StockConst.UPTICK_TYPE_UP:
                color = R.color.up;
                break;
            case StockConst.UPTICK_TYPE_DOWN:
                color = R.color.down;
                break;
            default:
                color = R.color.flat;
                break;
        }
        return color;
    }
}
