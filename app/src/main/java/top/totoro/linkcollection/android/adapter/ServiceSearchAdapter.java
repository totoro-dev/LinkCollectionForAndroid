package top.totoro.linkcollection.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import entry.SearchInfo;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.dialog.CollectDialog;
import top.totoro.linkcollection.android.dialog.DeleteDialog;
import top.totoro.linkcollection.android.ui.ShowActivity;
import top.totoro.linkcollection.android.ui.WebActivity;
import user.Info;

/**
 * Create by HLM on 2020-02-18
 * 用于显示从服务端搜索结果的适配器
 */
public class ServiceSearchAdapter extends RecyclerView.Adapter<ServiceSearchAdapter.ViewHolder> {

    private static final int TIPS_VIEW_TYPE = 1;
    private static final int ITEM_VIEW_TYPE = 2;
    private boolean noChooseLoves = false;
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
        if (viewType == TIPS_VIEW_TYPE) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.no_choose_loves, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == TIPS_VIEW_TYPE) {
            noChooseLoves = false;
            return;
        }
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
    public int getItemViewType(int position) {
        if (noChooseLoves && position == 0) {
            return TIPS_VIEW_TYPE;
        } else return ITEM_VIEW_TYPE;
    }

    /**
     * 提示用户未选择关注领域
     */
    public void notifyNoChooseLoves() {
        if (data != null) {
            data.clear();
        }
        noChooseLoves = true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (noChooseLoves) return 1;
        if (data == null) return 0;
        return data.size();
    }

    // 删除收藏的dialog
    private void alterDelete(BaseViewHolder holder, String title, String link, String labels) {
        DeleteDialog.newInstance(holder, title, link, labels).show(((BaseActivity) parent).getSupportFragmentManager(), "delete");
    }

    // 添加收藏的dialog
    private void alterAdd(BaseViewHolder holder, String title, String link, String l1, String l2, String l3) {
        CollectDialog.newInstance(holder, title, link, l1, l2, l3).show(((BaseActivity) parent).getSupportFragmentManager(), "add");
    }

    class ViewHolder extends BaseViewHolder {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 显示未选择关注的提示
            if (noChooseLoves) {
                TextView tvNoChooseLoves = itemView.findViewById(R.id.no_choose_loves);
                tvNoChooseLoves.setOnClickListener((v) -> {
                    ((ShowActivity) parent).toChooseLoveFragment();
                });
                return;
            }
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
