package com.fox.stockhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.fox.stockhelper.R;
import com.fox.stockhelper.api.login.LoginApi;
import com.fox.stockhelper.api.login.SendCodeApi;
import com.fox.stockhelper.config.ActivityResultCodeConfig;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.login.LoginApiDto;
import com.fox.stockhelper.exception.self.ApiException;
import com.fox.stockhelper.ui.base.BaseActivity;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
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
public class LoginActivity extends BaseActivity implements CommonHandleListener {
    /**
     * 是否记住账号key
     */
    static final String ACCOUNT_SAVE = "account_save";
    /**
     * 账号
     */
    static final String ACCOUNT_LOGIN = "account_login";
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
     *  是否记住账号
     */
    boolean accountSave;
    /**
     * 上下文
     */
    Context context;
    /**
     * 消息处理
     */
    Handler handler = new CommonHandler(this);
    /**
     * 启动登录界面的ui
     */
    String fromUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        context = getApplicationContext();
        accountSave = sharedPreferences.getBoolean(ACCOUNT_SAVE, true);
        saveAccountCB.setChecked(accountSave);
        String account = sharedPreferences.getString(ACCOUNT_LOGIN, "");
        if (!account.equals("")) {
            accountET.setText(account);
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("login");
        Log.e("login", bundle.toString());
        fromUi = bundle.getString("fromUI");
    }

    /**
     * 消息提示
     * @param message
     */
    public void toast(String message) {
        if (null != message && !message.equals("")) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setText(message);
            toast.show();
        }
    }

    /**
     * 登录完成
     */
    public void loginFinish() {
        Intent intent = new Intent();
        setResult(ActivityResultCodeConfig.LOGIN_SUCCESS, intent);
        finish();
    }

    @OnClick({R.id.sendCodeBtn, R.id.loginBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sendCodeBtn:
                sendCodeBtn.setEnabled(false);
                sendCodeBtn.setText(String.valueOf(sendCodeDelay));
                //开启发送验证码倒计时
                this.sendCodeDelay();
                //发送验证码
                this.sendCode();
                break;
            case R.id.loginBtn:
                //记录账号
                this.saveLoginInfo();
                //登录
                this.login();
                break;
        }
    }

    /**
     * 当用户正在输入账号时
     * @param text
     */
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

    /**
     * 当用户正在输入验证码时
     * @param text
     */
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

    /**
     * 开启发送验证码倒计时
     */
    private void sendCodeDelay() {
        Runnable sendCodeDelayRunnable = new Runnable() {
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
        handler.postDelayed(sendCodeDelayRunnable, 1000);
    }

    /**
     * 发送验证码
     */
    private void sendCode() {
        Runnable sendCodeRunnable = () -> {
            Message msg = new Message();
            msg.what = MsgWhatConfig.SEND_CODE;
            Bundle bundle = new Bundle();
            try {
                SendCodeApi sendCodeApi = new SendCodeApi();
                Map<String, Object> sendCodeParams = new HashMap<String, Object>(1);
                sendCodeParams.put("account", accountET.getText().toString());
                sendCodeApi.setParams(sendCodeParams);
                sendCodeApi.request();
                bundle.putString("message", "发送验证码成功");
            } catch (ApiException e) {
                bundle.putString("message", e.getMessage());
            }
            msg.setData(bundle);
            handler.sendMessage(msg);
        };
        new Thread(sendCodeRunnable).start();
    }

    /**
     * 记录登录信息
     */
    private void saveLoginInfo() {
        accountSave = saveAccountCB.isChecked();
        String account = accountET.getText().toString();
        spEditor.putBoolean(ACCOUNT_SAVE, accountSave);
        spEditor.putString(ACCOUNT_LOGIN, accountSave ? account : "");
        spEditor.commit();
    }

    /**
     * 登录
     */
    private void login() {
        Runnable loginRunnable = () -> {
            Message msg = new Message();
            msg.what = MsgWhatConfig.LOGIN;
            Bundle bundle = new Bundle();
            try {
                LoginApi loginApi = new LoginApi();
                Map<String, Object> loginParams = new HashMap<String, Object>(2);
                loginParams.put("account", accountET.getText().toString());
                loginParams.put("verifyCode", verifyCodeET.getText().toString());
                loginApi.setParams(loginParams);
                LoginApiDto loginApiDto = (LoginApiDto) loginApi.request();
                setLoginSession(loginApiDto.getSessionid(), loginApiDto.getExpireTime());
                bundle.putString("message", "登录成功");
            } catch (ApiException e) {
                bundle.putString("message", e.getMessage());
            }
            msg.setData(bundle);
            handler.sendMessage(msg);
        };
        new Thread(loginRunnable).start();
    }

    /**
     * 消息处理
     * @param message
     */
    @Override
    public void handleMessage(Message message) {
        Bundle bundle = message.getData();
        String msg = bundle.getString("message");
        switch (message.what) {
            case MsgWhatConfig.SEND_CODE:
                this.toast(msg);
                break;
            case MsgWhatConfig.LOGIN:
                this.toast(msg);
                this.loginFinish();
                break;
        }
    }
}
