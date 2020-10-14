package com.fox.stockhelper.entity.dto.api.stock.realtime;

import com.fox.stockhelper.util.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 股票分钟线图
 * @author lusongsong
 * @date 2020/9/17 17:31
 */
@Data
public class DealPriceLineApiDto {
    /**
     * 股票编码
     */
    String stockCode;
    /**
     * 股票名称
     */
    String stockName;
    /**
     * 日期
     */
    String dt;
    /**
     * 线图分钟数量
     */
    Integer nodeCount;
    /**
     * 昨日收盘价
     */
    BigDecimal preClosePrice;
    /**
     * 成交量
     */
    Long dealNum;
    /**
     * 线图
     */
    List<MinuteDealPriceInfoDto> lineNode;

    /**
     * 将数据转换成表格需要的格式
     * @return
     */
    public Map<String, Object> convertToRealTimeChartData() {
        Map<String, Object> realTimeChartData = new HashMap<>(2);
        List<List<Object>> minuteDataList = new ArrayList<>(lineNode.size());
        for (MinuteDealPriceInfoDto minuteDealPriceInfoDto : lineNode) {
            List<Object> minuteData = new ArrayList<>(5);
            minuteData.add(
                    DateUtil.getDateFromStr(
                            dt + " " + minuteDealPriceInfoDto.getTime(),
                            DateUtil.TIME_FORMAT_2
                    ).getTime()
            );
            minuteData.add(minuteDealPriceInfoDto.getPrice());
            minuteData.add(minuteDealPriceInfoDto.getAvgPrice());
            minuteData.add(minuteDealPriceInfoDto.getDealNum());
            minuteData.add(minuteDealPriceInfoDto.getAvgPrice());
            minuteDataList.add(minuteData);
        }
        realTimeChartData.put("data", minuteDataList);
        realTimeChartData.put("preClose", preClosePrice);
        return realTimeChartData;
    }
}
