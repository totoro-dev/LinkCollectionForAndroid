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

import monitor.Background;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.ui.CheckMailActivity;
import top.totoro.linkcollection.android.ui.LoginActivity;
import top.totoro.linkcollection.android.ui.ShowActivity;
import top.totoro.linkcollection.android.util.Logger;

/**
 * Create by HLM on 2020-02-14
 */
@SuppressWarnings("ALL")
public abstract class BaseActivity extends AppCompatActivity {

    public static final LinkedList<BaseActivity> contexts = new LinkedList<>();

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeakReference reference = new WeakReference(this);
        contexts.add(this);
        instance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contexts.pollLast();
        instance = contexts.peekLast();
    }

    public static BaseActivity instance;

    public static BaseActivity getInstance() {
        return instance;
    }

    /**
     * 定义从当前Activity跳转Activity的方式
     *
     * @param clazz 跳转的Activity
     */
    public void startActivity(Class clazz) {
        Logger.d(this, "from " + getClass().getSimpleName() + " to " + clazz.getSimpleName());
        startActivity(new Intent(this, clazz));
    }


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.LOGIN_SUCCESS:
                    startActivity(ShowActivity.class);
                    Toast.makeText(instance, R.string.had_login, Toast.LENGTH_LONG).show();
                    Background.startClipboardListener(); // 开始监听剪贴板内容变化
                    break;
                case Constants.LOGIN_FAILED:
                    startActivity(LoginActivity.class);
                    if (!(msg.obj instanceof String)) {
                        Toast.makeText(instance, R.string.please_login, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(instance, (String) msg.obj, Toast.LENGTH_LONG).show();
                    }
                    break;
                case Constants.REGISTER_SUCCESS:
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
                    String[] info = ((String) msg.obj).split(",");
                    if (info.length != 5) break; // 防止内容不匹配
                    String link = info[0];
                    String title = info[1];
                    if ("链接已收藏".equals(title)) {
                        BaseApplication.getInstance().notifyMe(title, link, null, null, null, -1);
                        break;
                    }
                    String l1 = info[2], l2 = info[3], l3 = info[4];
                    BaseApplication.getInstance().notifyMe(title, link, l1, l2, l3, (int) System.currentTimeMillis());
                    break;
            }
        }
    };

}
