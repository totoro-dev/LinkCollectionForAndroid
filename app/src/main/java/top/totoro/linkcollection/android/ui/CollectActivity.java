package top.totoro.linkcollection.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.util.FindView;
import top.totoro.linkcollection.android.util.Logger;
import user.Info;

/**
 * Create by HLM on 2020-02-15
 */
public class CollectActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvLink;
    private EditText etLabel1;
    private EditText etLabel2;
    private EditText etLabel3;
    private Button btnCollect;
    private Button btnCancel;
    private FindView find;

    private String link;
    private String title;

    @Override
    protected void onStart() {
        super.onStart();
        tvTitle = find.TextView(R.id.collect_info_title);
        tvLink = find.TextView(R.id.collect_info_link);
        etLabel1 = find.EditText(R.id.collect_info_label_1);
        etLabel2 = find.EditText(R.id.collect_info_label_2);
        etLabel3 = find.EditText(R.id.collect_info_label_3);
        btnCollect = find.Button(R.id.btn_collect_confirm);
        btnCancel = find.Button(R.id.btn_collect_cancel);
        btnCollect.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent == null) return;
        link = intent.getStringExtra("link");
        title = intent.getStringExtra("title");
        String l1 = intent.getStringExtra("l1");
        String l2 = intent.getStringExtra("l2");
        String l3 = intent.getStringExtra("l3");
        Logger.d(this, link, title, l1, l2, l3);
        tvTitle.setText(title);
        tvLink.setText(link);
        if (l1 != null && !"null".equals(l1)) {
            etLabel1.setText(l1);
        }
        if (l2 != null && !"null".equals(l2)) {
            etLabel2.setText(l2);
        }
        if (l3 != null && !"null".equals(l3)) {
            etLabel3.setText(l3);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        find = new FindView(findViewById(R.id.collect_layout));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_collect_confirm:
                String l1 = etLabel1.getText().toString();
                String l2 = etLabel2.getText().toString();
                String l3 = etLabel3.getText().toString();
                if (l1.equals("")) {
                    Toast.makeText(this, R.string.label_1_is_empty, Toast.LENGTH_SHORT).show();
                    break;
                }
                String labels = l1 + "," + (l2.equals("") ? "" : "," + l2) + (l3.equals("") ? "" : "," + l3);
                Info.addCollection(link, labels, title);
                Toast.makeText(this, R.string.collect_success, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_collect_cancel:
                finish();
                break;
        }
    }
}
