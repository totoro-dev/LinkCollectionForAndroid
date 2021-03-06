package top.totoro.linkcollection.android.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import entry.CollectionInfo;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.adapter.CollectionAdapter;
import top.totoro.linkcollection.android.adapter.LabelAdapter;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.base.Constants;
import top.totoro.linkcollection.android.util.FindView;
import top.totoro.linkcollection.android.util.Logger;
import user.Info;
import user.Login;

/**
 * Create by HLM on 2020-02-16
 */
public class MeFragment extends Fragment {

    private static final int REFRESH_SUCCESS = 1;
    private static final int REFRESH_FAILED = 2;
    private static final int REFRESH_TIMEOUT = 3;

    private static MeFragment instance;
    private static RecyclerView rvCollection; // 静态，避免空指针
    public final CollectionAdapter collectAdapter = new CollectionAdapter();
    private final LabelAdapter labelAdapter = new LabelAdapter();
    public boolean showingLabels = false;
    private SwipeRefreshLayout srlSwipe;
    private TextView tvNeverCollect;
    private Map<String, List<CollectionInfo>> itemMap = new LinkedHashMap<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_SUCCESS:
                    srlSwipe.setRefreshing(false);
                    if (showingLabels) {
                        showLabels();
                    } else {
                        collectAdapter.notifyDataSetChanged(Info.getCollectionInfos());
                        showingLabels = false;
                    }
                    break;
                case REFRESH_FAILED:
                    Toast.makeText(getContext(), R.string.refresh_failed, Toast.LENGTH_SHORT).show();
                    srlSwipe.setRefreshing(false);
                    break;
                case REFRESH_TIMEOUT:
                    Toast.makeText(getContext(), R.string.refresh_timeout, Toast.LENGTH_SHORT).show();
                    srlSwipe.setRefreshing(false);
                    break;
            }
        }
    };
    private FindView find;

    private Runnable timeoutTask = () -> handler.sendEmptyMessage(REFRESH_TIMEOUT);
    private ScheduledFuture future;

    public static MeFragment getInstance() {
        if (instance == null) instance = new MeFragment();
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();
        srlSwipe = find.View(SwipeRefreshLayout.class, R.id.show_collect_swipe);
        rvCollection = find.View(RecyclerView.class, R.id.show_collect_list);
        tvNeverCollect = find.TextView(R.id.never_collect_tips);
        Logger.d(this, "onStart() RecyclerView is null ? " + (rvCollection == null));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rvCollection.setLayoutManager(manager);
        checkHasCollect();
        if (showingLabels) {
            showLabels();
        } else {
            rvCollection.setAdapter(collectAdapter);
        }
        srlSwipe.setOnRefreshListener(() -> {
            if (showingLabels) {
                handler.sendEmptyMessage(REFRESH_SUCCESS);
                return;
            }
            new Thread(() -> {
                if (Login.getUserId() != null && !"".equals(Login.getUserId())) {
                    future = Executors.newScheduledThreadPool(1).schedule(timeoutTask, 10000, TimeUnit.MILLISECONDS);
                    if (Info.refreshCollectionInfo(Login.getUserId()) && Info.getCollectionInfo()) {
                        handler.sendEmptyMessage(REFRESH_SUCCESS);
                    } else {
                        handler.sendEmptyMessage(REFRESH_FAILED);
                    }
                    future.cancel(true);
                    return;
                }
                BaseActivity.getInstance().handler.sendEmptyMessage(Constants.LOGIN_FAILED);
            }).start();
        });
    }

    public void showCollect() {
        checkHasCollect();
        rvCollection.setAdapter(collectAdapter);
        showingLabels = false;
    }

    public void showLabels() {
        checkHasCollect();
        refreshLabelData();
        showingLabels = true;
    }

    /**
     * 刷新所有收藏链接的列表
     *
     * @param data 新的数据
     */
    public void refreshCollectData(List<CollectionInfo> data) {
        if (checkHasCollect()) {
            collectAdapter.notifyDataSetChanged(data);
        }
    }

    public void refreshLabelData() {
        LinkedList<String> labelList = new LinkedList<>();
        Info.getCollectionInfo();
        checkHasCollect();
        if (Info.getCollectionInfos() == null || Info.getCollectionInfos().size() == 0) return;
        itemMap.clear();
        String labelJoin = "";
        for (CollectionInfo info :
                Info.getCollectionInfos()) {
            String[] ls = info.getLabels();
            for (String l :
                    ls) {
                if (labelJoin.equals("," + l) || labelJoin.endsWith("," + l) || labelJoin.contains("," + l + ",")) {
//                    continue;
                } else {
                    labelJoin += "," + l;
                    labelList.add(l);
                }
                List<CollectionInfo> list = itemMap.get(l);
                if (list == null) {
                    list = new LinkedList<>();
                    itemMap.put(l, list);
                }
                list.add(info);
            }
        }
        labelAdapter.setItemMap(itemMap);
        labelAdapter.notifyDataSetChanged(labelList, itemMap.get(labelAdapter.selectLabelName));
        if (showingLabels) {
            rvCollection.setAdapter(labelAdapter);
        }
    }

    /**
     * 确定是否已有链接收藏，没有的话显示提示信息
     *
     * @return true，已有收藏的链接；false，暂无收藏的链接
     */
    private boolean checkHasCollect() {
        if (Info.getCollectionCount() == 0) {
            tvNeverCollect.setVisibility(View.VISIBLE);
        } else {
            tvNeverCollect.setVisibility(View.GONE);
        }
        return tvNeverCollect.getVisibility() == View.GONE;
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(this, "onResume()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this, "onCreateView()");
        collectAdapter.setContext(container.getContext());
        labelAdapter.setContext(container.getContext());
        View view = inflater.inflate(R.layout.fragment_pager_me, container, false);
        find = new FindView(view);
        return view;
    }

}
