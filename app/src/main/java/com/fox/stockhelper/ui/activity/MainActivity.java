package com.fox.stockhelper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.config.ActivityRequestCodeConfig;
import com.fox.stockhelper.ui.base.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * APP入口类
 * @author lusongsong
 */
public class MainActivity extends BaseActivity {
    /**
     * 主界面显示框
     */
    @BindView(R.id.mainTV)
    TextView mainTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        //用户未登录，则跳转到登录页面
        if (!this.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("fromUI", getLocalClassName());
            intent.putExtra("login", bundle);
            startActivityForResult(intent, ActivityRequestCodeConfig.LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("main2222", "aaaaaa");
        Log.e("main1111", String.valueOf(resultCode));
        switch (resultCode) {
            case 1:
                Log.e("main", this.getLoginSession());
                mainTV.setText(this.getLoginSession());
                break;
        }
    }
}
