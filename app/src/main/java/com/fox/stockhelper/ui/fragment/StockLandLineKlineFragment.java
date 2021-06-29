package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.po.nets.NetsDayDealInfoPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.offline.DealDayApiDto;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealInfoApiDto;
import com.fox.stockhelper.ui.base.BaseFragment;
import com.fox.stockhelper.ui.view.StockDealInfoView;
import com.fox.stockhelper.util.DateUtil;
import com.fox.stockhelperchart.StockKLineChart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 天周月线图
 *
 * @author lusongsong
 * @date 2020/10/22 20:38
 */
public class StockLandLineKlineFragment extends BaseFragment {
    /**
     * 股票id
     */
    StockVo stockVo;
    /**
     * 线图类型
     */
    Integer dateType;
    /**
     * 按天价格数据
     */
    List<NetsDayDealInfoPo> dealDayInfoList;
    boolean land = true;

    @BindView(R.id.stockDealInfoSDIV)
    StockDealInfoView stockDealInfoSDIV;
    /**
     * 按天价格数据
     */
    List<DealDayApiDto> dealDayApiDtoList;
    /**
     * k线图
     */
    @BindView(R.id.stockKLineChart)
    StockKLineChart stockKLineChart;

    /**
     * 指定上下文构造器
     *
     * @param context
     */
    public StockLandLineKlineFragment(Context context, StockVo stockVo, Integer dateType) {
        super(context);
        this.stockVo = stockVo;
        this.dateType = dateType;
    }

    /**
     * 创建视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_land_line_kline, null);
        ButterKnife.bind(this, view);
        stockKLineChart.setDateType(dateType);
        stockKLineChart.initChart(stockVo);
        return view;
    }

    /**
     * 数据转图标
     *
     * @return
     */
    private String listToChartData() {
        if (null == dealDayInfoList) {
            return null;
        }
        List chartDataList = new ArrayList();
        List<BigDecimal> closePriceList = new ArrayList<>(60);
        List<Integer> maList = Arrays.asList(5, 10, 20, 30, 60);
        Map<Integer, BigDecimal> sumClosePriceMap = new HashMap<>(maList.size());
        for (Integer ma : maList) {
            sumClosePriceMap.put(ma, new BigDecimal(0));
        }
        for (int i = 0; i < dealDayInfoList.size(); i++) {
            NetsDayDealInfoPo netsDayDealInfoPo = dealDayInfoList.get(i);
            if (null != netsDayDealInfoPo) {
                List<Object> dataList = new ArrayList<>();
                dataList.add(DateUtil.getDateFromStr(netsDayDealInfoPo.getDt()).getTime());
                dataList.add(netsDayDealInfoPo.getOpenPrice().toString());
                dataList.add(netsDayDealInfoPo.getHighestPrice().toString());
                dataList.add(netsDayDealInfoPo.getLowestPrice().toString());
                dataList.add(netsDayDealInfoPo.getClosePrice().toString());
                dataList.add(netsDayDealInfoPo.getDealNum().toString());
                dataList.add(netsDayDealInfoPo.getDealMoney().toString());
                closePriceList.add(netsDayDealInfoPo.getClosePrice());
                for (Integer ma : maList) {
                    BigDecimal sumPrice = sumClosePriceMap.get(ma);
                    sumPrice = sumPrice.add(netsDayDealInfoPo.getClosePrice());
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
                dataList.add(netsDayDealInfoPo.getPreClosePrice().toString());
                chartDataList.add(dataList);
            }
        }
        Map<String, List> priceDayMap = new HashMap<>(1);
        priceDayMap.put("data", chartDataList);
        return com.alibaba.fastjson.JSONObject.toJSONString(priceDayMap);
    }

    /**
     * 获取周月范围列表
     *
     * @return
     * @throws ParseException
     */
    public List<String> doDateByStatisticsType() throws ParseException {
        List<String> listWeekOrMonth = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = "1990-12-01";
        String endDate = DateUtil.getCurrentDate();
        Date sDate = dateFormat.parse(startDate);
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        sCalendar.setTime(sDate);
        Date eDate = dateFormat.parse(endDate);
        Calendar eCalendar = Calendar.getInstance();
        eCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        eCalendar.setTime(eDate);
        boolean bool = true;
        switch (dateType) {
            case StockConst.DT_WEEK:
                while (sCalendar.getTime().getTime() < eCalendar.getTime().getTime()) {
                    if (bool || sCalendar.get(Calendar.DAY_OF_WEEK) == 2 || sCalendar.get(Calendar.DAY_OF_WEEK) == 1) {
                        listWeekOrMonth.add(dateFormat.format(sCalendar.getTime()));
                        bool = false;
                    }
                    sCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
                if (listWeekOrMonth.size() % 2 != 0) {
                    listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
                }
                break;
            case StockConst.DT_MONTH:
                while (sCalendar.getTime().getTime() < eCalendar.getTime().getTime()) {
                    if (bool || sCalendar.get(Calendar.DAY_OF_MONTH) == 1 || sCalendar.get(Calendar.DAY_OF_MONTH) == sCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        listWeekOrMonth.add(dateFormat.format(sCalendar.getTime()));
                        bool = false;
                    }
                    sCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
                if (listWeekOrMonth.size() % 2 != 0) {
                    listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
                }
                break;

        }
        return listWeekOrMonth;
    }

    /**
     * 选中
     *
     * @param index
     */
    public void choose(Integer index) {
        DealInfoApiDto dealInfoApiDto = new DealInfoApiDto();
        NetsDayDealInfoPo netsDayDealInfoPo = dealDayInfoList.get(index);
        dealInfoApiDto.setCurrentPrice(netsDayDealInfoPo.getClosePrice());
        dealInfoApiDto.setPreClosePrice(netsDayDealInfoPo.getPreClosePrice());
        dealInfoApiDto.setOpenPrice(netsDayDealInfoPo.getOpenPrice());
        dealInfoApiDto.setHighestPrice(netsDayDealInfoPo.getHighestPrice());
        dealInfoApiDto.setLowestPrice(netsDayDealInfoPo.getLowestPrice());
        dealInfoApiDto.setDealNum(netsDayDealInfoPo.getDealNum());
        dealInfoApiDto.setDealMoney(netsDayDealInfoPo.getDealMoney());
        stockDealInfoSDIV.setData(dealInfoApiDto).reDraw();
    }
}
