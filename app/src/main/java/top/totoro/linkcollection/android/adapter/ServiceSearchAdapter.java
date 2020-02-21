package top.totoro.linkcollection.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import entry.SearchInfo;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.dialog.CollectDialog;
import top.totoro.linkcollection.android.dialog.DeleteDialog;
import top.totoro.linkcollection.android.ui.WebActivity;
import user.Info;

/**
 * Create by HLM on 2020-02-18
 * 用于显示从服务端搜索结果的适配器
 */
public class ServiceSearchAdapter extends RecyclerView.Adapter<ServiceSearchAdapter.ViewHolder> {

    private List<SearchInfo> data;
    private Context parent;

    public void setContext(Context context) {
        parent = context;
    }

    public void notifyDataSetChanged(List<SearchInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchInfo info = data.get(position);
        holder.linkId = info.getLinkId();
        holder.tvTitle.setText(info.getTitle());
        holder.tvLink.setText(info.getLink());
        holder.tvLabels.setText(String.join(" | ", info.getLabels().split(",")));
        if (Info.checkHasCollected(Long.parseLong(info.getLinkId()))) {
            holder.ivCollect.setBackgroundResource(R.mipmap.collected40x40);
            holder.collected = true;
        } else {
            holder.ivCollect.setBackgroundResource(R.mipmap.collect40x40);
            holder.collected = false;
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private void alterDelete(BaseViewHolder holder, String title, String link, String labels) {
        DeleteDialog.newInstance(holder, title, link, labels).show(((BaseActivity) parent).getSupportFragmentManager(), "delete");
    }

    private void alterAdd(BaseViewHolder holder, String title, String link, String l1, String l2, String l3) {
        CollectDialog.newInstance(holder, title, link, l1, l2, l3).show(((BaseActivity) parent).getSupportFragmentManager(), "add");
    }

    class ViewHolder extends BaseViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            setLayoutOnClickListener(v -> {
                WebActivity.link = tvLink.getText().toString();
                Intent intent = new Intent(parent, WebActivity.class);
                parent.startActivity(intent);
            });
            setCollectOnClickListener(v -> {
                if (collected) {
                    alterDelete(this, tvTitle.getText().toString(), tvLink.getText().toString(), tvLabels.getText().toString());
                } else {
                    String[] ls = tvLabels.getText().toString().split("\\|");
                    String l1 = "", l2 = "", l3 = "";
                    for (int i = 0; i < ls.length; i++) {
                        if (i == 0) {
                            l1 = ls[0].trim();
                        } else if (i == 1) {
                            l2 = ls[1].trim();
                        } else {
                            l3 = ls[2].trim();
                        }
                    }
                    alterAdd(this, tvTitle.getText().toString(), tvLink.getText().toString(), l1, l2, l3);
                }
            });
        }
    }
}