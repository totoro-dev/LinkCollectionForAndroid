package top.totoro.linkcollection.android.test.adapter;

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

import top.totoro.linkcollection.android.test.R;

/**
 * Create by HLM on 2020-02-17
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<String> data = new LinkedList<>();

    public Adapter() {
        for (int i = 0; i < 30; i++) {
            data.add("第" + i + "项");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        LinearLayout right;
        TextView tv;
        ImageView item_right;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.ll_layout);
            right = itemView.findViewById(R.id.right);
            tv = itemView.findViewById(R.id.text);
            item_right = itemView.findViewById(R.id.item_right);
            ll.setClickable(true);
        }
    }

}
