package top.totoro.linkcollection.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.result.MyCheckMailResult;
import top.totoro.linkcollection.android.result.MyLinkResult;
import top.totoro.linkcollection.android.result.MyLocalSearch;
import top.totoro.linkcollection.android.result.MyLoginResult;
import top.totoro.linkcollection.android.result.MyMonitorResult;
import top.totoro.linkcollection.android.result.MyPublishResult;
import top.totoro.linkcollection.android.result.MyRegisterResult;
import top.totoro.linkcollection.android.result.MyUserResult;
import user.Login;
import utils.CommonUtil;

public class MainActivity extends BaseActivity {

    private static final int AUTO_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.checkAppCommon(new MyUserResult(), new MyPublishResult(), new MyLinkResult(), new MyLoginResult(),
                new MyRegisterResult(), new MyCheckMailResult(), new MyMonitorResult(), new MyLocalSearch());
        handler.sendEmptyMessageDelayed(AUTO_LOGIN, 1200);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == AUTO_LOGIN) {
                Login.autoLogin();
                Log.d(MainActivity.class.getSimpleName(), "handleMessage: auto login");
            }
            finish();
        }
    };
}
