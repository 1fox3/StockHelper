package com.fox.stockhelperchart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.fox.stockhelperchart.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * 单日分钟K线图按钮
     */
    Button minuteKLineBtn;
    /**
     * 5日K线图按钮
     */
    Button fiveDayKLineBtn;
    /**
     * K线图按钮
     */
    Button kLineBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        minuteKLineBtn = findViewById(R.id.minuteKLineBtn);
        fiveDayKLineBtn = findViewById(R.id.fiveDayKLineBtn);
        kLineBtn = findViewById(R.id.kLineBtn);
        minuteKLineBtn.setOnClickListener(this);
        fiveDayKLineBtn.setOnClickListener(this);
        kLineBtn.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Class desClass = null;
        int id = v.getId();
        if (id == R.id.minuteKLineBtn) {
            desClass = SingleDayMinuteChartActivity.class;
        } else if (id == R.id.fiveDayKLineBtn) {
            desClass = MultiDayMinuteChartActivity.class;
        } else {
            desClass = KLineActivity.class;
        }
        startActivity(new Intent(MainActivity.this, desClass));
    }
}
