package top.totoro.linkcollection.android.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.ui.ShowActivity;
import user.Info;

/**
 * Create by HLM on 2020-03-06
 */
public class MyInformationDialog extends DialogFragment implements View.OnClickListener {

    private static MyInformationDialog instance;
    private ShowActivity activity;

    private LinearLayout collectCountLayout;
    private LinearLayout lovesLayout;

    public static MyInformationDialog getInstance() {
        if (instance == null) instance = new MyInformationDialog();
        return instance;
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

    public void show(ShowActivity activity) {
        this.activity = activity;
        super.show(activity.getSupportFragmentManager(), "information");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_my_information, container, false);
        collectCountLayout = view.findViewById(R.id.information_collect_count_layout);
        lovesLayout = view.findViewById(R.id.information_loves_layout);
        collectCountLayout.setOnClickListener(this);
        lovesLayout.setOnClickListener(this);
        long count = Info.getCollectionCount();
        String loves = Info.getLovesInChinese(Info.getLoveInfo()).replace(",", " | ");
        ((TextView) view.findViewById(R.id.information_collect_count)).setText(count + "个链接");
        ((TextView) view.findViewById(R.id.information_loves)).setText(loves);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.information_collect_count_layout:
                dismiss();
                activity.showAllCollectLinks();
                break;
            case R.id.information_loves_layout:
                dismiss();
                activity.toChooseLoveFragment();
                break;
        }
    }
}
