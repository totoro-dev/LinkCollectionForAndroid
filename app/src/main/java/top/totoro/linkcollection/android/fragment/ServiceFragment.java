package top.totoro.linkcollection.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import entry.SearchInfo;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.adapter.ServiceSearchAdapter;
import top.totoro.linkcollection.android.util.FindView;
import top.totoro.linkcollection.android.util.Logger;

/**
 * Create by HLM on 2020-02-16
 */
public class ServiceFragment extends Fragment {

    private static ServiceFragment instance;
    public final ServiceSearchAdapter adapter = new ServiceSearchAdapter();
    private RecyclerView rvCollection;
    private FindView find;

    public ServiceFragment() {
        instance = this;
    }

    public static ServiceFragment getInstance() {
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();
        rvCollection = find.View(RecyclerView.class, R.id.show_service_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rvCollection.setLayoutManager(manager);
        rvCollection.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter.setContext(view.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_service, container, false);
        find = new FindView(view);
        return view;
    }

    public void refreshData(List<SearchInfo> data) {
        Logger.d(this, "refreshCollectData() : " + data.size());
        adapter.notifyDataSetChanged(data);
    }
}
