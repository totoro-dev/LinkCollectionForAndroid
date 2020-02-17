package top.totoro.linkcollection.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import entry.CollectionInfo;
import top.totoro.linkcollection.android.R;
import user.Info;

/**
 * Create by HLM on 2020-02-16
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private List<CollectionInfo> data;

    public void notifyDataSetChanged(List<CollectionInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public CollectionAdapter() {
        this.data = Info.getCollectionInfos();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectionInfo info = data.get(position);
        holder.tvTitle.setText(info.getTitle());
        holder.tvLink.setText(info.getLink());
        holder.tvLabels.setText(String.join(" | ", info.getLabels()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvLink;
        TextView tvLabels;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_title);
            tvLink = itemView.findViewById(R.id.item_link);
            tvLabels = itemView.findViewById(R.id.item_labels);
        }
    }
}
