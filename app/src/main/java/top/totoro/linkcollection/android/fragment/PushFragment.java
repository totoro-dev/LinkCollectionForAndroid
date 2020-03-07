package top.totoro.linkcollection.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import entry.SearchInfo;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.adapter.ServiceSearchAdapter;
import top.totoro.linkcollection.android.base.Constants;
import top.totoro.linkcollection.android.util.FindView;
import top.totoro.linkcollection.android.util.Logger;
import user.Info;

/**
 * Create by HLM on 2020-02-16
 */
public class PushFragment extends Fragment implements View.OnClickListener {

    private static final ServiceSearchAdapter adapter = new ServiceSearchAdapter();
    private static final int ON_RESUME = -9;
    private static final int CHECK_LOVE_INFO = -10;
    private Map<String, LinkedList<SearchInfo>> dataMap = new ConcurrentHashMap<>(10);
    private LinkedList<SearchInfo> data = new LinkedList<>();
    private List<TextView> titles = new LinkedList<>();
    private int currentTitle = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.GET_PUSH_SUCCESS:
                    refreshData(data);
                    break;
                case CHECK_LOVE_INFO:
                    checkLoveInfo();
                    break;
                case ON_RESUME:
                    initTitleOnResume();
                    break;
            }
        }
    };
    private RecyclerView rvCollection;
    private FindView find;

    private void refreshData(List<SearchInfo> data) {
        Logger.d(this, "refreshCollectData() : " + data.size());
        adapter.notifyDataSetChanged(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(this, "onStart()");
        initView();
        initListener();
        initTitleOnResume();
        rvCollection.setAdapter(adapter);
    }

    private void initView() {
        titles.clear();
        titles.add(find.TextView(R.id.recommend));
        titles.add(find.TextView(R.id.art));
        titles.add(find.TextView(R.id.science));
        titles.add(find.TextView(R.id.computer));
        titles.add(find.TextView(R.id.healthy));
        titles.add(find.TextView(R.id.economics));
        titles.add(find.TextView(R.id.life));
        titles.add(find.TextView(R.id.game));
        titles.add(find.TextView(R.id.eat));
        titles.add(find.TextView(R.id.tour));
        rvCollection = find.View(RecyclerView.class, R.id.show_push_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rvCollection.setLayoutManager(manager);
    }

    private void initListener() {
        for (TextView title : titles) {
            title.setOnClickListener(this);
        }
    }

    /**
     * 进入页面时，初始化标题栏的选中条目
     */
    private void initTitleOnResume() {
        if (isAdded()) {
            titles.get(currentTitle).setTextColor(getResources().getColor(R.color.colorPrimary));
            titles.get(currentTitle).setTextSize(20);
        } else {
            handler.sendEmptyMessageDelayed(ON_RESUME, 100);
        }
    }

    /**
     * 要显示推荐内容时，检测是否已有选择的关注领域
     * 没有的话会提示进入关注页进行选择关注
     */
    public void checkLoveInfo() {
        if (isAdded()) {
            String info = Info.getLoveInfo();
            Logger.d(this, "checkLoveInfo() position = 0 , love info = " + (info == null ? "null" : info));
            if (info == null || info.length() == 0) {
                adapter.notifyNoChooseLoves();
                titles.get(currentTitle).setTextColor(getResources().getColor(R.color.colorPrimary));
                titles.get(currentTitle).setTextSize(20);
            } else {
                refreshTitle(info);
            }
        } else {
            handler.sendEmptyMessageDelayed(CHECK_LOVE_INFO, 100);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_pager_push, container, false);
        find = new FindView(view);
        if (container == null) return view;
        adapter.setContext(container.getContext());
        return view;
    }

    @Override
    public void onClick(View v) {
        titles.get(currentTitle).setTextColor(getResources().getColor(R.color.colorPushTitleTextColor));
        titles.get(currentTitle).setTextSize(19);
        switch (v.getId()) {
            case R.id.recommend:
                currentTitle = 0;
                checkLoveInfo();
                break;
            case R.id.art:
                currentTitle = 1;
                refreshTitle("art");
                break;
            case R.id.science:
                currentTitle = 2;
                refreshTitle("science");
                break;
            case R.id.computer:
                currentTitle = 3;
                refreshTitle("computer");
                break;
            case R.id.healthy:
                currentTitle = 4;
                refreshTitle("healthy");
                break;
            case R.id.economics:
                currentTitle = 5;
                refreshTitle("economics");
                break;
            case R.id.life:
                currentTitle = 6;
                refreshTitle("life");
                break;
            case R.id.game:
                currentTitle = 7;
                refreshTitle("game");
                break;
            case R.id.eat:
                currentTitle = 8;
                refreshTitle("eat");
                break;
            case R.id.tour:
                currentTitle = 9;
                refreshTitle("tour");
                break;
        }
    }

    private void refreshTitle(String type) {
        titles.get(currentTitle).setTextColor(getResources().getColor(R.color.colorPrimary));
        titles.get(currentTitle).setTextSize(20);
        new Thread(() -> { // 开启线程获取数据
            if ((data = dataMap.get(type)) == null) {
                data = Info.getPushContent(type);
                dataMap.put(type, data);
            }
            handler.sendEmptyMessage(Constants.GET_PUSH_SUCCESS);
        }).start();
    }
}
