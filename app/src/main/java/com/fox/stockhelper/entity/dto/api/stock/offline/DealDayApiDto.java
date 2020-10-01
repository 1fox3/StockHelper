package com.fox.stockhelper.entity.dto.api.stock.offline;

import com.fox.stockhelper.util.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 股票按天交易数据
 * @author lusongsong
 * @date 2020/10/1 14:03
 */
@Data
public class DealDayApiDto {
    /**
     * 日期
     */
    String dt;
    /**
     * 开盘价
     */
    BigDecimal openPrice;
    /**
     * 收盘价
     */
    BigDecimal closePrice;
    /**
     * 最高价
     */
    BigDecimal highestPrice;
    /**
     * 最低价
     */
    BigDecimal lowestPrice;
    /**
     * 上个交易日收盘价
     */
    BigDecimal preClosePrice;
    /**
     * 成交量
     */
    Long dealNum;
    /**
     * 成交金额
     */
    BigDecimal dealMoney;
    /**
     * 流通股本数
     */
    Long circEquity;
    /**
     * 总股本数
     */
    Long totalEquity;

    public static List<String> fieldOrderList() {
        return Arrays.asList(
                "dt",
                "openPrice",
                "closePrice",
                "highestPrice",
                "lowestPrice",
                "preClosePrice",
                "dealNum",
                "dealMoney",
                "circEquity",
                "totalEquity"
        );
    }

    /**
     * 接口数据转为图片数据
     * @param dealDayApiDtoList
     * @return
     */
    public static List listToChartData(List<DealDayApiDto> dealDayApiDtoList) {
        if (null == dealDayApiDtoList) {
            return null;
        }
        List chartDataList = new ArrayList();
        List<BigDecimal> closePriceList = new ArrayList<>(60);
        List<Integer> maList = Arrays.asList(5, 10, 20, 30, 60);
        Map<Integer, BigDecimal> sumClosePriceMap = new HashMap<>(maList.size());
        for (Integer ma : maList) {
            sumClosePriceMap.put(ma, new BigDecimal(0));
        }
        for (int i = 0; i < dealDayApiDtoList.size(); i++) {
            DealDayApiDto dealDayApiDto = dealDayApiDtoList.get(i);
            if (null != dealDayApiDto) {
                List<Object> dataList = new ArrayList<>();
                dataList.add(DateUtil.getDateFromStr(dealDayApiDto.getDt()).getTime());
                dataList.add(dealDayApiDto.getOpenPrice().toString());
                dataList.add(dealDayApiDto.getHighestPrice().toString());
                dataList.add(dealDayApiDto.getLowestPrice().toString());
                dataList.add(dealDayApiDto.getClosePrice().toString());
                dataList.add(dealDayApiDto.getDealNum().toString());
                dataList.add(dealDayApiDto.getDealMoney().toString());
                closePriceList.add(dealDayApiDto.getClosePrice());
                for (Integer ma : maList) {
                    BigDecimal sumPrice = sumClosePriceMap.get(ma);
                    sumPrice = sumPrice.add(dealDayApiDto.getClosePrice());
                    if (ma <= closePriceList.size()) {
                        sumPrice = sumPrice.subtract(
                                closePriceList.get(closePriceList.size() - ma)
                        );
                        dataList.add(sumPrice.divide(
                                new BigDecimal(ma), 2, RoundingMode.HALF_UP)
                        );
                    } else {
                        dataList.add(sumPrice.divide(
                                new BigDecimal(closePriceList.size()), 2, RoundingMode.HALF_UP)
                        );
                    }
                    sumClosePriceMap.put(ma, sumPrice);
                }
                dataList.add(dealDayApiDto.getPreClosePrice().toString());
                chartDataList.add(dataList);
            }
        }
        return chartDataList;
    }
}
