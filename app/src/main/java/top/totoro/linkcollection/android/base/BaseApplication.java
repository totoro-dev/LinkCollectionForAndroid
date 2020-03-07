package top.totoro.linkcollection.android.base;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import java.io.File;

import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.ui.ShowActivity;
import top.totoro.linkcollection.android.util.Logger;

/**
 * Create by HLM on 2020-02-14
 */
public class BaseApplication extends Application {

    public static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获取剪切板的内容
     *
     * @return 内容
     */
    public String getClipboardContent() {
        ClipboardManager cbm = (ClipboardManager) instance.getSystemService(CLIPBOARD_SERVICE);
        if (cbm == null) {
            Logger.d(this, "getClipboardContent()", "ClipboardManager is null");
            return "";
        }
        ClipData data = cbm.getPrimaryClip();
        if (data != null && data.getItemCount() > 0) {
            return data.getItemAt(0).getText().toString();
        }
        return "";
    }

    /**
     * 发送链接收藏的通知
     *
     * @param title       标题
     * @param link        链接
     * @param l1          热门标签1
     * @param l2          热门标签2
     * @param l3          热门标签3
     * @param collectCode 收藏标志/通知标志：-1表示已收藏
     */
    public void notifyMe(String title, String link, String l1, String l2, String l3, long collectCode) {
        int id = (int) System.currentTimeMillis();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager == null) {
            Logger.d(this, "notifyMe()", "NotificationManager is null");
            return;
        }
        // Android8以上需要设置通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id + "", "chanel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.enableLights(true);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id + "");
        builder.setSmallIcon(R.mipmap.app);
        builder.setContentTitle(title);
        builder.setContentText(link);
        builder.setTicker("有新通知");
        builder.setAutoCancel(true);
        builder.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Suite.ogg")));
        builder.setVibrate(new long[]{0, 1000, 250, 1000});
        Intent intent = new Intent(this, ShowActivity.class);
        Logger.d(this, "notify code = " + collectCode);
        // 处理通知跳转页面时发送的信息
        if (collectCode > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("link", link);
            bundle.putString("l1", l1);
            bundle.putString("l2", l2);
            bundle.putString("l3", l3);
            intent.putExtras(bundle);
        }
        builder.setContentIntent(PendingIntent.getActivity(this, (int)collectCode, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        Notification notification = builder.build();
        manager.notify(id, notification);
    }

}
