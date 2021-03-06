package top.totoro.linkcollection.android.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.base.BaseActivity;

/**
 * Create by HLM on 2020-02-20
 */
public class AboutDialog extends DialogFragment {

    private static AboutDialog instance;

    public static AboutDialog getInstance() {
        if (instance == null) instance = new AboutDialog();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_about, container, false);
    }
}
