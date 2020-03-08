package top.totoro.linkcollection.android.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import entry.CollectionInfo;
import monitor.Background;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.dialog.LoadingDialog;
import top.totoro.linkcollection.android.ui.CheckMailActivity;
import top.totoro.linkcollection.android.ui.LoginActivity;
import top.totoro.linkcollection.android.ui.ShowActivity;
import top.totoro.linkcollection.android.util.Logger;

/**
 * Create by HLM on 2020-02-14
 * 处理start Activity的各项工作
 * 接收各种观察者模式下的接口返回，通过handler机制
 */
@SuppressWarnings("ALL")
public abstract class BaseActivity extends AppCompatActivity {

    private static final LinkedList<BaseActivity> contexts = new LinkedList<>();
    public static BaseActivity instance;
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            LoadingDialog.getInstance().dismiss();
            switch (msg.what) {
                case Constants.AUTO_LOGIN_FAILED:
                    startActivity(LoginActivity.class);
                    break;
                case Constants.LOGIN_SUCCESS:
                    startActivity(ShowActivity.class);
                    Toast.makeText(instance, R.string.had_login, Toast.LENGTH_LONG).show();
                    Background.startClipboardListener(); // 开始监听剪贴板内容变化
                    break;
                case Constants.LOGIN_FAILED:
                    if (!(msg.obj instanceof String)) {
                        Toast.makeText(instance, R.string.please_login, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(instance, (String) msg.obj, Toast.LENGTH_LONG).show();
                    }
                    break;
                case Constants.REGISTER_SUCCESS:
                    CheckMailActivity.checkType = CheckMailActivity.REGISTER;
                    startActivity(CheckMailActivity.class);
                    Toast.makeText(instance, R.string.please_check_mail, Toast.LENGTH_LONG).show();
                    break;
                case Constants.REGISTER_FAILED:
                    if (!(msg.obj instanceof String)) {
                        Toast.makeText(instance, R.string.register_failed, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(instance, (String) msg.obj, Toast.LENGTH_LONG).show();
                    }
                    break;
                case Constants.CHECK_MAIL_SUCCESS:
                    Logger.d("MyCheckMailResult", "start activity");
                    startActivity(LoginActivity.class);
                    Toast.makeText(instance, R.string.check_mail_success, Toast.LENGTH_LONG).show();
                    break;
                case Constants.CHECK_MAIL_FAILED:
                    if (!(msg.obj instanceof String)) {
                        Toast.makeText(instance, R.string.check_mail_failed, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(instance, (String) msg.obj, Toast.LENGTH_LONG).show();
                    }
                    break;
                case Constants.SPIDER_SUCCESS:
                    CollectionInfo info = (CollectionInfo) msg.obj;
                    String link = info.getLink();
                    String title = info.getTitle();
                    if ("链接已收藏".equals(title)) {
                        BaseApplication.getInstance().notifyMe(title, link, null, null, null, -1);
                        break;
                    }
                    String[] ls = info.getLabels();
                    String l1 = ls[0] == null || ls[0].equals("null") ? "" : ls[0];
                    String l2 = ls[1] == null || ls[1].equals("null") ? "" : ls[1];
                    String l3 = ls[2] == null || ls[2].equals("null") ? "" : ls[2];
                    Logger.d(instance, "current system time = " + System.currentTimeMillis());
                    BaseApplication.getInstance().notifyMe(title, link, l1, l2, l3, System.currentTimeMillis());
                    break;
                case Constants.COLLECT_SUCCESS:
                    Toast.makeText(instance == null ? BaseActivity.this : instance, R.string.collect_success, Toast.LENGTH_LONG).show();
                    break;
                case Constants.UPDATE_PWD_SEND_CODE:
                    String result = String.valueOf(msg.obj);
                    if (result.equals("验证码已发送")) {
                        CheckMailActivity.checkType = CheckMailActivity.UPDATE_PWD;
                        startActivity(CheckMailActivity.class);
                        Toast.makeText(instance, R.string.please_check_mail, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(instance, result, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    public static void pollActivity(Class<?> clazz) {
        while (contexts.size() > 0) {
            Object context = contexts.pollLast();
            if (context == null) continue;
            Logger.d(BaseActivity.class, "pollActivity() : " + context.getClass().getSimpleName());
            if (context instanceof BaseActivity) ((BaseActivity) context).finish();
            // 栈中存在指定的Activity
            if (context.getClass().getSimpleName().equals(clazz.getSimpleName())) {
                break;
            }
        }
    }

    public static BaseActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        WeakReference reference = new WeakReference(this);
        contexts.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contexts.pollLast();
        instance = contexts.peekLast();
    }

    /**
     * 定义从当前Activity跳转Activity的方式
     *
     * @param clazz 跳转的Activity
     */
    public void startActivity(Class clazz) {
        Logger.d(this, "from " + instance.getClass().getSimpleName() + " to " + clazz.getSimpleName());
        instance.startActivity(new Intent(instance, clazz));
    }

}
