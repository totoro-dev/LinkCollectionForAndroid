package top.totoro.linkcollection.android.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import top.totoro.linkcollection.android.R;

/**
 * Create by HLM on 2020-03-07
 * 显示加载框
 */
public class LoadingDialog extends DialogFragment {

    private static LoadingDialog instance;
    private Animation loading;

    public static LoadingDialog getInstance() {
        if (instance == null) instance = new LoadingDialog();
        return instance;
    }

    public void show(FragmentManager manager) {
        show(manager, "loading");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            //设置背景半透明
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View view = inflater.inflate(R.layout.dialog_loading, container, false);
        loading = AnimationUtils.loadAnimation(view.getContext(), R.anim.loading);
        view.setAnimation(loading);
        loading.start();
        return view;
    }

    @Override
    public void dismiss() {
        // 如果动画已开启，需要关闭动画
        if (loading != null && loading.hasStarted()) loading.cancel();
        super.dismiss();
    }
}
