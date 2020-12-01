package com.fox.stockhelper.entity.dto.api.stock.stockmarket;

import lombok.Data;

/**
 * 股市交易日接口
 *
 * @author lusongsong
 * @date 2020/11/30 16:55
 */
@Data
public class AroundDealDateApiDto {
    /**
     * 当前交易日
     */
    String last;
    /**
     * 下一个交易日
     */
    String next;
    /**
     * 上一个交易日
     */
    String pre;
}
