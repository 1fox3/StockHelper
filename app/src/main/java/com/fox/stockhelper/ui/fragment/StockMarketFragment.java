package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.constant.stock.StockMarketStatusConst;
import com.fox.stockhelper.serv.stock.StockMarketStatusServ;
import com.fox.stockhelper.ui.base.BaseFragment;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.util.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * 股市首页
 * @author lusongsong
 */
public class StockMarketFragment extends BaseFragment implements CommonHandleListener {
    /**
     * 股市
     */
    private String stockMarket;
    /**
     * 股市状态logo
     */
    @BindView(R.id.smStatusIV)
    ImageView smStatusIV;
    /**
     * 股市状态文案
     */
    @BindView(R.id.smStatusTV)
    TextView smStatusTV;
    /**
     * 当前日期
     */
    @BindView(R.id.smDate)
    TextView smDate;
    /**
     * 星期
     */
    @BindView(R.id.smWeek)
    TextView smWeek;

    /**
     * 股票排行
     */
    @BindView(R.id.stockRankHeader)
    LinearLayout stockRankHeader;

    /**
     * 消息处理
     */
    Handler handler = new CommonHandler(this);

    /**
     * 指定上下文和股市
     * @param context
     * @param stockMarket
     */
    public StockMarketFragment(Context context, String stockMarket) {
        super(context);
        this.stockMarket = stockMarket;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_market, null);
        ButterKnife.bind(this, view);
        //显示时间
        this.initDate();
        //股市状态
        this.handleStockMarketStatus();
        return view;
    }

    /**
     * 设置显示时间
     */
    private void initDate() {
        smDate.setText(DateUtil.getCurrentDate());
        smWeek.setText(DateUtil.getCurrentWeekNum());
    }

    /**
     * 消息处理
     * @param message
     */
    @Override
    public void handleMessage(Message message) {
        Bundle bundle = message.getData();
        switch (message.what) {
            case MsgWhatConfig.SM_STATUS:
                int smStatus = bundle.getInt("smStatus");
                int[] smStatusInfo = StockMarketStatusConst.getStatusInfo(smStatus);
                smStatusIV.setImageResource(smStatusInfo[0]);
                smStatusTV.setText(smStatusInfo[1]);
        }
    }

    /**
     * 开启定时检查交易状态
     */
    private void handleStockMarketStatus() {
        Runnable stockMarketStatusRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    int smStatus = StockMarketStatusServ.getStockMarketStatus();
                    Message msg = new Message();
                    msg.what = MsgWhatConfig.SM_STATUS;
                    Bundle bundle = new Bundle();
                    bundle.putInt("smStatus", smStatus);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    Thread.sleep(600000);
                }
            }
        };
        new Thread(stockMarketStatusRunnable).start();
    }
}
