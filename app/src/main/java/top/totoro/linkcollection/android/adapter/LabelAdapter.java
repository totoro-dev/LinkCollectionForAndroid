package top.totoro.linkcollection.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import entry.CollectionInfo;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.dialog.CollectDialog;
import top.totoro.linkcollection.android.dialog.DeleteDialog;
import top.totoro.linkcollection.android.ui.WebActivity;
import top.totoro.linkcollection.android.util.Logger;
import user.Info;

/**
 * Create by HLM on 2020-02-19
 */
public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.ViewHolder> {

    public static LabelAdapter instance;
    private final int HEADER = 0;
    private final int LABELER = 1;
    private final int MAINER = 2;
    private final int FOOTER = 3;
    public String selectLabelName = "";
    private int showingPosition = -1;
    private List<String> data = new LinkedList<>();
    private LinkedList<String> labelList = new LinkedList<>();
    private List<CollectionInfo> itemData = new LinkedList<>();
    private Map<String, List<CollectionInfo>> itemMap;
    private Context parent;

    public LabelAdapter() {
        instance = this;
    }

    public void setItemMap(Map<String, List<CollectionInfo>> itemMap) {
        this.itemMap = itemMap;
    }

    public void notifyDataSetChanged(List<String> data, List<CollectionInfo> itemData) {
        if (showingPosition >= data.size()) {
            for (int i = 0; i < this.data.size(); i++) {
                showingPosition--;
                if (showingPosition < data.size()) {
                    break;
                }
            }
        }
        if (data != null) {
            this.data = data;
        }
        this.data = data;
        if (itemData != null) {
            this.itemData = itemData;
        } else {
            this.itemData.clear();
        }
        notifyDataSetChanged();
    }

    public void setContext(Context context) {
        parent = context;
    }

    private void alterDelete(RecyclerView.ViewHolder holder, String title, String link, String labels) {
        DeleteDialog.newInstance(holder, title, link, labels).show(((BaseActivity) parent).getSupportFragmentManager(), "delete");
    }

    private void alterAdd(RecyclerView.ViewHolder holder, String title, String link, String l1, String l2, String l3) {
        CollectDialog.newInstance(holder, title, link, l1, l2, l3).show(((BaseActivity) parent).getSupportFragmentManager(), "add");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.labels, parent, false));
            case LABELER:
                return new LabelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_label, parent, false));
            case MAINER:
                return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
            case FOOTER:
                return new FootViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.labels, parent, false));
        }
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER:
                ((HeadViewHolder) holder).tvLabel.setText(data.get(position));
                ((HeadViewHolder) holder).llLabelLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectLabelName = ((HeadViewHolder) holder).tvLabel.getText().toString();
                        showingPosition = position;
                        Logger.d("LabelAdapter", "showing position : " + showingPosition);
                        notifyDataSetChanged(data, itemMap.get(selectLabelName));
                    }
                });
                break;
            case LABELER:
                ((LabelViewHolder) holder).tvLabel.setText(data.get(position));
                break;
            case MAINER:
                CollectionInfo info = itemData.get(position - showingPosition - 1);
                ((MainViewHolder) holder).linkId = info.getLinkId();
                ((MainViewHolder) holder).tvTitle.setText(info.getTitle());
                ((MainViewHolder) holder).tvLink.setText(info.getLink());
                ((MainViewHolder) holder).tvLabels.setText(String.join(" | ", info.getLabels()));
                if (Info.checkHasCollected(Long.parseLong(info.getLinkId()))) {
                    ((MainViewHolder) holder).ivCollect.setBackgroundResource(R.mipmap.collected40x40);
                    ((MainViewHolder) holder).collected = true;
                } else {
                    ((MainViewHolder) holder).ivCollect.setBackgroundResource(R.mipmap.collect40x40);
                    ((MainViewHolder) holder).collected = false;
                }
                break;
            case FOOTER:
                ((FootViewHolder) holder).tvLabel.setText(data.get(position - itemData.size()));
                ((FootViewHolder) holder).llLabelLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showingPosition = position - itemData.size();
                        Logger.d("LabelAdapter", "showing position : " + showingPosition);
                        selectLabelName = ((FootViewHolder) holder).tvLabel.getText().toString();
                        notifyDataSetChanged(data, itemMap.get(selectLabelName));
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (showingPosition < 0) {
            return HEADER;
        }
        if (position < showingPosition) {
            return HEADER;
        } else if (position == showingPosition) {
            return LABELER;
        } else if (position <= showingPosition + itemData.size()) {
            return MAINER;
        } else {
            return FOOTER;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + itemData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class HeadViewHolder extends ViewHolder {
        LinearLayout llLabelLayout;
        TextView tvLabel;

        HeadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.label);
            llLabelLayout = itemView.findViewById(R.id.show_labels_layout);
        }

    }

    public class MainViewHolder extends ViewHolder {
        public ImageView ivCollect;
        public boolean collected = true;
        public String linkId = "";
        LinearLayout layout;
        TextView tvTitle;
        TextView tvLink;
        TextView tvLabels;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_layout);
            tvTitle = itemView.findViewById(R.id.item_title);
            tvLink = itemView.findViewById(R.id.item_link);
            tvLabels = itemView.findViewById(R.id.item_labels);
            ivCollect = itemView.findViewById(R.id.item_collect);
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

        void setLayoutOnClickListener(View.OnClickListener listener) {
            layout.setOnClickListener(listener);
        }

        void setCollectOnClickListener(View.OnClickListener listener) {
            ivCollect.setOnClickListener(listener);
        }
    }

    class FootViewHolder extends ViewHolder {
        TextView tvLabel;
        LinearLayout llLabelLayout;

        FootViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.label);
            llLabelLayout = itemView.findViewById(R.id.show_labels_layout);
        }
    }

    class LabelViewHolder extends ViewHolder {
        TextView tvLabel;

        LabelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.label);
        }
    }
}
