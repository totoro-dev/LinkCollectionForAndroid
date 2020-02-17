package top.totoro.linkcollection.android.util;

import android.util.Log;

/**
 * Create by HLM on 2020-02-14
 */
public class Logger {
    public static void d(Object target, Object... msgs) {
        String t = target instanceof String ? (String) target : target.getClass().getSimpleName();
        if (msgs.length > 0) {
            if (msgs.length != 1) {
                Log.d(t, "----------log start----------");
            }
            for (Object msg :
                    msgs) {
                Log.d(t, msg == null ? "message is null" : msg.toString());
            }
            if (msgs.length != 1) {
                Log.d(t, "----------log end----------");
            }
        }
    }
}
