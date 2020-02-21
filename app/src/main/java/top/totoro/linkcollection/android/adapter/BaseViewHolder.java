package top.totoro.linkcollection.android.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import top.totoro.linkcollection.android.R;

/**
 * Create by HLM on 2020-02-18
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivCollect;
    public boolean collected = true;
    public String linkId = "";
    LinearLayout layout;
    TextView tvTitle;
    TextView tvLink;
    TextView tvLabels;

    BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        layout = itemView.findViewById(R.id.item_layout);
        tvTitle = itemView.findViewById(R.id.item_title);
        tvLink = itemView.findViewById(R.id.item_link);
        tvLabels = itemView.findViewById(R.id.item_labels);
        ivCollect = itemView.findViewById(R.id.item_collect);
    }

    protected void setLayoutOnClickListener(View.OnClickListener listener) {
        layout.setOnClickListener(listener);
    }

    protected void setCollectOnClickListener(View.OnClickListener listener) {
        ivCollect.setOnClickListener(listener);
    }
}
