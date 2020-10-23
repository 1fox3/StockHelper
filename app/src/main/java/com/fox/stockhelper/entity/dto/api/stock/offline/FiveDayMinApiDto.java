package com.fox.stockhelper.entity.dto.api.stock.offline;

import com.fox.stockhelper.util.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 近5天分钟数据
 * @author lusongsong
 * @date 2020/10/23 16:27
 */
@Data
public class FiveDayMinApiDto {
    /**
     * 日期
     */
    String dt;
    /**
     * 时间
     */
    String time;
    /**
     * 开盘价
     */
    BigDecimal price;
    /**
     * 收盘价
     */
    BigDecimal avgPrice;
    /**
     * 成交金额
     */
    Long dealNum;

    public static List<String> fieldOrderList() {
        return Arrays.asList(
                "dt",
                "time",
                "price",
                "avgPrice",
                "dealNum"
        );
    }

    /**
     * 将数据转换成表格需要的格式
     * @return
     */
    public static Map<String, Object> convertToFiveDayChartData(
            List<FiveDayMinApiDto> fiveDayMinApiDtoList
    ) {
        BigDecimal preClosePrice = null;
        Map<String, Object> realTimeChartData = new HashMap<>(2);
        List<List<Object>> minuteDataList = new ArrayList<>(fiveDayMinApiDtoList.size());
        int i = 0;
        for (FiveDayMinApiDto fiveDayMinApiDto : fiveDayMinApiDtoList) {
            List<Object> minuteData = new ArrayList<>(5);
            minuteData.add(
                    DateUtil.getDateFromStr(
                            fiveDayMinApiDto.getDt() + " " + fiveDayMinApiDto.getTime(),
                            DateUtil.TIME_FORMAT_1
                    ).getTime()
            );
            minuteData.add(fiveDayMinApiDto.getPrice().toString());
            minuteData.add(fiveDayMinApiDto.getAvgPrice().toString());
            minuteData.add(fiveDayMinApiDto.getDealNum().toString());
            minuteData.add(fiveDayMinApiDto.getAvgPrice().toString());
            if (i % 4 == 0) {
                minuteDataList.add(minuteData);
            }
            i++;
            if (null == preClosePrice) {
                preClosePrice = fiveDayMinApiDto.getPrice();
            }
        }
        realTimeChartData.put("data", minuteDataList);
        realTimeChartData.put("preClose", preClosePrice.toString());
        return realTimeChartData;
    }
}
