package top.totoro.linkcollection.android.util;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 通过该类获取对应类型的视图元素
 * Create by HLM on 2020-01-15
 */
public class FindView {

    private View parent;

    public FindView(View parent) {
        this.parent = parent;
    }

    private View findView(int id) {
        View view = parent.findViewById(id);
        if (view == null)
            throw new RuntimeException("findView(int id): 当前界面不存在资源ID为" + id + "的视图元素。");
        return view;
    }

    public RelativeLayout RelativeLayout(int id) {
        View view = findView(id);
        if (view instanceof RelativeLayout) return (RelativeLayout) view;
        else
            throw new RuntimeException("findRelativeLayout(int id): 资源ID为" + id + "的视图无法转换成RelativeLayout");
    }

    public LinearLayout LinearLayout(int id) {
        View view = findView(id);
        if (view instanceof LinearLayout) return (LinearLayout) view;
        else
            throw new RuntimeException("findLinearLayout(int id): 资源ID为" + id + "的视图无法转换成LinearLayout");
    }

    public TextView TextView(int id) {
        View view = findView(id);
        if (view instanceof TextView) return (TextView) view;
        else
            throw new RuntimeException("findTextView(int id): 资源ID为" + id + "的视图无法转换成TextView");
    }

    public RadioButton RadioButton(int id) {
        View view = findView(id);
        if (view instanceof RadioButton) return (RadioButton) view;
        else
            throw new RuntimeException("findRadioButton(int id): 资源ID为" + id + "的视图无法转换成RadioButton");
    }

    public EditText EditText(int id) {
        View view = findView(id);
        if (view instanceof EditText) return (EditText) view;
        else
            throw new RuntimeException("findEditText(int id): 资源ID为" + id + "的视图无法转换成EditText");
    }

    public ImageView ImageView(int id) {
        View view = findView(id);
        if (view instanceof ImageView) return (ImageView) view;
        else
            throw new RuntimeException("findImageView(int id): 资源ID为" + id + "的视图无法转换成ImageView");
    }

    public ImageButton ImageButton(int id) {
        View view = findView(id);
        if (view instanceof ImageButton) return (ImageButton) view;
        else
            throw new RuntimeException("findImageButton(int id): 资源ID为" + id + "的视图无法转换成ImageButton");
    }

    public Button Button(int id) {
        View view = findView(id);
        if (view instanceof Button) return (Button) view;
        else
            throw new RuntimeException("findButton(int id): 资源ID为" + id + "的视图无法转换成Button");
    }

    public <T> T View(Class<T> clazz, int id) {
        View view = findView(id);
        if (view != null) {
            return (T) view;
        } else
            throw new RuntimeException("findButton(int id): 资源ID为" + id + "的视图无法转换成Button");
    }

}
