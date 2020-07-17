package com.fox.stockhelper.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.fox.stockhelper.R;
import com.fox.stockhelper.api.login.SendCodeApi;
import com.fox.stockhelper.ui.base.BaseActivity;
import com.fox.stockhelper.util.ParamCheckUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 登录
 *
 * @author lusongsong
 */
public class LoginActivity extends BaseActivity {
    /**
     * 是否记住账号key
     */
    final static String ACCOUNT_SAVE = "account_save";
    /**
     * 账号
     */
    final static String ACCOUNT_LOGIN = "account_login";
    /**
     * 发送验证码延时间隔
     */
    private int sendCodeDelay = 60;
    /**
     * 账号输入框
     */
    @BindView(R.id.accountET)
    EditText accountET;
    /**
     * 验证码输入框
     */
    @BindView(R.id.verifyCodeET)
    EditText verifyCodeET;
    /**
     * 发送验证码入口
     */
    @BindView(R.id.sendCodeBtn)
    Button sendCodeBtn;
    /**
     * 保存账号
     */
    @BindView(R.id.saveAccountCB)
    CheckBox saveAccountCB;
    /**
     * 登录按钮
     */
    @BindView(R.id.loginBtn)
    Button loginBtn;
    /**
     * 验证码倒计时消息处理
     */
    Handler handler;
    /**
     * 登录相关数据记录
     */
    SharedPreferences sharedPreferences;
    /**
     *  是否记住账号
     */
    boolean accountSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        sharedPreferences = getSharedPreferences(getLocalClassName(), MODE_PRIVATE);
        accountSave = sharedPreferences.getBoolean(ACCOUNT_SAVE, true);
        saveAccountCB.setChecked(accountSave);
        String account = sharedPreferences.getString(ACCOUNT_LOGIN, "");
        if (!account.equals("")) {
            accountET.setText(account);
        }
    }

    @OnClick({R.id.sendCodeBtn, R.id.loginBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sendCodeBtn:
                sendCodeBtn.setEnabled(false);
                sendCodeBtn.setText(String.valueOf(sendCodeDelay));
                handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        int currentDelay = Integer.valueOf(sendCodeBtn.getText().toString());
                        if (currentDelay > 1) {
                            sendCodeBtn.setText(String.valueOf(currentDelay - 1));
                            handler.postDelayed(this, 1000);
                        } else {
                            sendCodeBtn.setEnabled(true);
                            sendCodeBtn.setText("重新获取");
                        }
                    }
                };
                handler.postDelayed(runnable, 1000);
                SendCodeApi sendCodeApi = new SendCodeApi();
                Map<String, Object> sendCodeParams = new HashMap<String, Object>(1);
                sendCodeParams.put("account", accountET.getText().toString());
                sendCodeApi.request();
                break;
            case R.id.loginBtn:
                //记录账号
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                accountSave = saveAccountCB.isChecked();
                String account = accountET.getText().toString();
                spEditor.putBoolean(ACCOUNT_SAVE, accountSave);
                spEditor.putString(ACCOUNT_LOGIN, accountSave ? account : "");
                spEditor.commit();
                //登录
                break;
        }
    }

    @OnTextChanged(R.id.accountET)
    public void onAccountETTextChanged(CharSequence text) {
        String account = text.toString();
        //符合邮箱验证规则
        if (ParamCheckUtil.isEmail(account)) {
            sendCodeBtn.setEnabled(true);
        } else {
            sendCodeBtn.setEnabled(false);
            loginBtn.setEnabled(false);
        }
    }

    @OnTextChanged(R.id.verifyCodeET)
    public void onVerifyCodeETTextChanged(CharSequence text) {
        String account = accountET.getText().toString();
        String verifyCode = text.toString();
        //符合验证码规则
        if (ParamCheckUtil.isEmail(account) && ParamCheckUtil.isVerifyCode(verifyCode)) {
            loginBtn.setEnabled(true);
        } else {
            loginBtn.setEnabled(false);
        }
    }
}
