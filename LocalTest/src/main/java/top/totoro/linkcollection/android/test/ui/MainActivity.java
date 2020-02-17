package top.totoro.linkcollection.android.test.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    private RecyclerView rvTest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian);
        rvTest = (RecyclerView)findViewById(R.id.rvTest);
        rvTest.setLayoutManager(new LinearLayoutManager(this));
        rvTest.setAdapter(new Adapter());
        rvTest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                    if (foodsArrayList.get(firstItemPosition) instanceof Foods) {
//                        int foodTypePosion = ((Foods) foodsArrayList.get(firstItemPosition)).getFood_stc_posion();
//                        FoodsTypeListview.getChildAt(foodTypePosion).setBackgroundResource(R.drawable.choose_item_selected);
//                    }
                    System.out.println(lastItemPosition + "   " + firstItemPosition);
                }
            }
        });
        setSupportActionBar(((Toolbar)findViewById(R.id.tool)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(getClass().getSimpleName(),"onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.menu_tool_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    ScheduledFuture<String> future;

    public void click(View view) {
        Executors.newScheduledThreadPool(1).schedule(()->{
//            startActivity(new Intent(this,TwoActivity.class));
//            handler.sendEmptyMessage(0);
            notifyMe();
        },2000,TimeUnit.MILLISECONDS);
//        Callable<String> callable = new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                String title = new LinkSpider("https://baidu.com").getTitle();
//                return title;
//            }
//        };
//        future = Executors.newScheduledThreadPool(1).schedule(callable, 0, TimeUnit.MILLISECONDS);
//        try {
//            String title = future.get();
//            Log.d("MainActivity", title == null ? "爬取失败" : title);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private void showAlterDialog(){
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(MainActivity.this);
        alterDiaglog.setIcon(R.drawable.ic_launcher_background);//图标
        alterDiaglog.setTitle("简单的dialog");//文字
        alterDiaglog.setMessage("生存还是死亡");//提示消息
        //积极的选择
        alterDiaglog.setPositiveButton("生存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"点击了生存",Toast.LENGTH_SHORT).show();
            }
        });
        //消极的选择
        alterDiaglog.setNegativeButton("死亡", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"点击了死亡",Toast.LENGTH_SHORT).show();
            }
        });
        //中立的选择
        alterDiaglog.setNeutralButton("不生不死", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"点击了不生不死",Toast.LENGTH_SHORT).show();
            }
        });

        //显示
        alterDiaglog.show();
    }

    public void notifyMe(){
        int id = (int) System.currentTimeMillis();
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(id+"","chanel",NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.enableLights(true);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id+"");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("通知");
        builder.setContentText("Hello World");
        builder.setTicker("有新通知");
        builder.setAutoCancel(true);
        builder.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Suite.ogg")));
        builder.setVibrate(new long[]{0,1000,250,1000});
        Bundle bundle = new Bundle();
        bundle.putString("link","https://baidu.com");
        Intent intent = new Intent(this,TwoActivity.class);
        intent.putExtras(bundle);
        builder.setContentIntent(PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        Notification notification = builder.build();
        manager.notify(id,notification);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
//            showAlterDialog();
            notifyMe();
        }
    };
}
