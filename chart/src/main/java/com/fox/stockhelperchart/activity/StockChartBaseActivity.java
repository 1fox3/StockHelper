package com.fox.stockhelperchart.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.vo.StockVo;

/**
 * @author lusongsong
 * @date 2021/5/24 17:25
 */
public class StockChartBaseActivity extends AppCompatActivity {
    public static final StockVo SH_TEST_STOCK = new StockVo("603383", StockConst.SM_A);
}
