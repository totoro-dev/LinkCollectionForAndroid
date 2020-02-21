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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.adapter.BaseViewHolder;
import top.totoro.linkcollection.android.adapter.LabelAdapter;
import top.totoro.linkcollection.android.fragment.MeFragment;
import top.totoro.linkcollection.android.fragment.ServiceFragment;
import top.totoro.linkcollection.android.util.FindView;
import top.totoro.linkcollection.android.util.Logger;
import user.Info;

/**
 * Create by HLM on 2020-02-18
 */
public class DeleteDialog extends DialogFragment implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvLink;
    private TextView tvLabels;
    private TextView tvDelete;
    private TextView tvCancel;
    private FindView find;
    private RecyclerView.ViewHolder holder;
    private Context context;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (holder != null) {
                if (holder.getClass().isAssignableFrom(BaseViewHolder.class)) {
                    ((BaseViewHolder) holder).ivCollect.setBackgroundResource(R.mipmap.collect40x40);
                    ((BaseViewHolder) holder).collected = false;
                } else if (holder.getClass().isAssignableFrom(LabelAdapter.MainViewHolder.class)) {
                    Logger.d("DeleteDialog", "delete at labels : " + tvLabels.getText());
                    ((LabelAdapter.MainViewHolder) holder).ivCollect.setBackgroundResource(R.mipmap.collect40x40);
                    ((LabelAdapter.MainViewHolder) holder).collected = false;
                    MeFragment.getInstance().refreshLabelData();
                }
            }
            Toast.makeText(context, R.string.delete_success, Toast.LENGTH_LONG).show();
            MeFragment.getInstance().collectAdapter.notifyDataSetChanged();
            ServiceFragment.getInstance().adapter.notifyDataSetChanged();
        }
    };

    public static DeleteDialog newInstance(RecyclerView.ViewHolder holder, String title, String link, String labels) {
        DeleteDialog fragment = new DeleteDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("link", link);
        bundle.putString("labels", labels);
        fragment.setArguments(bundle);
        fragment.holder = holder;
        return fragment;
    }

    @Override
    public void onResume() {
        // 设置宽度
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        find = new FindView(view);
        tvTitle = find.TextView(R.id.delete_title);
        tvLink = find.TextView(R.id.delete_link);
        tvLabels = find.TextView(R.id.delete_labels);
        tvDelete = find.TextView(R.id.tv_delete_confirm);
        tvCancel = find.TextView(R.id.tv_delete_cancel);
        if (getArguments() == null) return;
        tvTitle.setText(getArguments().getString("title"));
        tvLink.setText(getArguments().getString("link"));
        tvLabels.setText(getArguments().getString("labels"));
        tvDelete.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_delete, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete_confirm:
                new Thread(() -> {
                    if (holder != null) {
                        if (holder.getClass().isAssignableFrom(BaseViewHolder.class)) {
                            Info.deleteCollection(((BaseViewHolder) holder).linkId);
                        } else if (holder.getClass().isAssignableFrom(LabelAdapter.MainViewHolder.class)) {
                            Info.deleteCollection(((LabelAdapter.MainViewHolder) holder).linkId);
                        }
                    }
                    handler.sendEmptyMessage(0);
                }).start();
                dismiss();
                break;
            case R.id.tv_delete_cancel:
                dismiss();
                break;
        }
    }
}
