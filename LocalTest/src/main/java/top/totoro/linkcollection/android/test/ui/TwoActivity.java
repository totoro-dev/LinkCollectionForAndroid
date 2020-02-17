package top.totoro.linkcollection.android.test.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import top.totoro.linkcollection.android.test.R;

/**
 * Create by HLM on 2020-02-15
 */
public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian);
        Log.d(getClass().getSimpleName(),getIntent().getStringExtra("link"));
    }
}
