package top.totoro.linkcollection.android.test.ui;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import top.totoro.linkcollection.android.test.R;
import top.totoro.linkcollection.android.test.adapter.Adapter;

/**
 * Create by HLM on 2020-02-15
 */
public class MainActivity extends AppCompatActivity {

    ScheduledFuture<String> future;
    private RecyclerView rvTest;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian);
        rvTest = (RecyclerView) findViewById(R.id.rvTest);
        rvTest.setLayoutManager(new LinearLayoutManager(this));
        rvTest.setAdapter(new Adapter());
        setSupportActionBar(((Toolbar) findViewById(R.id.tool)));
        CollectDialog.newInstance("title").show(getSupportFragmentManager(), "test");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(getClass().getSimpleName(), "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void click(View view) {
        Executors.newScheduledThreadPool(1).schedule(() -> {
            notifyMe();
        }, 2000, TimeUnit.MILLISECONDS);
    }

    public void notifyMe() {
        int id = (int) System.currentTimeMillis();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id + "", "chanel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.enableLights(true);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id + "");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("通知");
        builder.setContentText("Hello World");
        builder.setTicker("有新通知");
        builder.setAutoCancel(true);
        builder.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Suite.ogg")));
        builder.setVibrate(new long[]{0, 1000, 250, 1000});
        Bundle bundle = new Bundle();
        bundle.putString("link", "https://baidu.com");
        Intent intent = new Intent(this, TwoActivity.class);
        intent.putExtras(bundle);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        Notification notification = builder.build();
        manager.notify(id, notification);
    }

}
