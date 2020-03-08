package top.totoro.linkcollection.android.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.adapter.BaseViewHolder;
import top.totoro.linkcollection.android.adapter.ServiceSearchAdapter;
import top.totoro.linkcollection.android.fragment.MeFragment;
import top.totoro.linkcollection.android.fragment.PushFragment;
import top.totoro.linkcollection.android.fragment.ServiceFragment;
import top.totoro.linkcollection.android.util.FindView;
import top.totoro.linkcollection.android.util.Logger;
import user.Info;

/**
 * Create by HLM on 2020-02-18
 * 进行链接收藏的编辑对话框
 */
public class CollectDialog extends DialogFragment implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvLink;
    private EditText etLabel1;
    private EditText etLabel2;
    private EditText etLabel3;
    private TextView tvCollect;
    private TextView tvCancel;
    private FindView find;
    private RecyclerView.ViewHolder holder;
    private Context context;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (holder == null) { // 从通知进来
                Toast.makeText(context, R.string.collect_success, Toast.LENGTH_LONG).show();
                MeFragment.getInstance().refreshCollectData(Info.getCollectionInfos());
            } else {
                if (holder instanceof ServiceSearchAdapter.ViewHolder) {
                    ((BaseViewHolder) holder).ivCollect.setBackgroundResource(R.mipmap.collected40x40);
                    ((BaseViewHolder) holder).collected = true;
                }
                Toast.makeText(context, R.string.collect_success, Toast.LENGTH_LONG).show();
                Info.getCollectionInfo();
                MeFragment.getInstance().refreshCollectData(Info.getCollectionInfos());
                MeFragment.getInstance().refreshLabelData();
//                PushFragment.getInstance().refreshData();
//                ServiceFragment.getInstance().refreshData();
            }
            ServiceFragment.getInstance().adapter.notifyDataSetChanged();
        }
    };

    public static CollectDialog newInstance(RecyclerView.ViewHolder holder, String title, String link, String l1, String l2, String l3) {
        CollectDialog fragment = new CollectDialog();
        // 接收链接的基本信息，当界面初始化的时候就可以提取
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("link", link);
        bundle.putString("l1", l1);
        bundle.putString("l2", l2);
        bundle.putString("l3", l3);
        fragment.setArguments(bundle);
        fragment.holder = holder;
        return fragment;
    }

    @Override
    public void onResume() {
        // 设置宽度
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT; // 对话框宽度要设置屏幕的宽度，但是dialog本身与机身边框有一定间距
            getDialog().getWindow().setAttributes(params);
        }
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        find = new FindView(view);
        tvTitle = find.TextView(R.id.collect_info_title);
        tvLink = find.TextView(R.id.collect_info_link);
        etLabel1 = find.EditText(R.id.collect_info_label_1);
        etLabel2 = find.EditText(R.id.collect_info_label_2);
        etLabel3 = find.EditText(R.id.collect_info_label_3);
        tvCollect = find.TextView(R.id.tv_collect_confirm);
        tvCancel = find.TextView(R.id.tv_collect_cancel);
        // 从这个dialog提取要收藏的链接信息
        if (getArguments() != null) {
            tvTitle.setText(getArguments().getString("title"));
            tvLink.setText(getArguments().getString("link"));
            if (!"".equals(getArguments().getString("l1"))) {
                etLabel1.setText(getArguments().getString("l1"));
            }
            if (!"".equals(getArguments().getString("l2"))) {
                etLabel2.setText(getArguments().getString("l2"));
            }
            if (!"".equals(getArguments().getString("l2"))) {
                etLabel3.setText(getArguments().getString("l3"));
            }
        }
        tvCollect.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_collect, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_collect_confirm:
                String l1 = etLabel1.getText().toString();
                String l2 = etLabel2.getText().toString();
                String l3 = etLabel3.getText().toString();
                if (l1.equals("")) {
                    Toast.makeText(getContext(), R.string.label_1_is_empty, Toast.LENGTH_SHORT).show();
                    break;
                }
                String labels = l1 + (l2.equals("") ? "" : "," + l2) + (l3.equals("") ? "" : "," + l3);
                new Thread(() -> {
                    Logger.d(this, tvLink.getText().toString());
                    Info.addCollection(tvLink.getText().toString(), labels, tvTitle.getText().toString());
                    handler.sendEmptyMessage(0);
                }).start();
                dismiss();
                break;
            case R.id.tv_collect_cancel:
                dismiss();
                break;
        }
    }
}
