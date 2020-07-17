package com.fox.stockhelper.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.base.BaseActivity;

/**
 * APP入口类
 * @author lusongsong
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //用户未登录，则跳转到登录页面
        if (!this.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
    }
}
