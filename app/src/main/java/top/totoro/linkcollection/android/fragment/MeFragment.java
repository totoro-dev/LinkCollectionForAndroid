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

import entry.CollectionInfo;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.adapter.CollectionAdapter;
import top.totoro.linkcollection.android.util.FindView;

/**
 * Create by HLM on 2020-02-16
 */
public class MeFragment extends Fragment {

    private RecyclerView rvCollection;
    private FindView find;

    private static CollectionAdapter adapter = new CollectionAdapter();;

    @Override
    public void onStart() {
        super.onStart();
        rvCollection = find.View(RecyclerView.class, R.id.show_collect_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rvCollection.setLayoutManager(manager);
        rvCollection.setAdapter(adapter);
    }

    public void refreshData(List<CollectionInfo> data){
        adapter.notifyDataSetChanged(data);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_me, container, false);
        find = new FindView(view);
        return view;
    }

}
