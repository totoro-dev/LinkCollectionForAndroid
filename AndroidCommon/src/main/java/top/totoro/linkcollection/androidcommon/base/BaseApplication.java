package top.totoro.linkcollection.androidcommon.base;

import android.app.Application;
import android.content.Context;

/**
 * Create by HLM on 2020-02-14
 */
public class BaseApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
