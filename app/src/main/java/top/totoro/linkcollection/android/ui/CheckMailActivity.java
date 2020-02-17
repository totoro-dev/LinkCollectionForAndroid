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
import user.Register;

/**
 * Create by HLM on 2020-02-14
 */
public class CheckMailActivity extends BaseActivity implements View.OnClickListener {

    private EditText etVerificationCode;
    private Button btnCheck;
    private TextView tvToLogin;
    private FindView find;

    @Override
    protected void onStart() {
        super.onStart();
        etVerificationCode = find.EditText(R.id.verification_code);
        btnCheck = find.Button(R.id.btn_check_mail);
        tvToLogin = find.TextView(R.id.from_check_mail_to_login);
        btnCheck.setOnClickListener(this);
        tvToLogin.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mail);
        find = new FindView(findViewById(R.id.check_mail_layout));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_mail:
                String code = etVerificationCode.getText().toString();
                if ("".equals(code)) {
                    Toast.makeText(CheckMailActivity.this, R.string.verification_code_is_empty, Toast.LENGTH_LONG).show();
                } else if (code.length() != 4) {
                    Toast.makeText(CheckMailActivity.this, R.string.verification_code_length_error, Toast.LENGTH_LONG).show();
                } else {
                    new Thread(() -> {
                        Register.checkMail(code);
                    }).start();
                }
                break;
            case R.id.from_check_mail_to_login:
                startActivity(new Intent(CheckMailActivity.this, LoginActivity.class));
                break;
        }
    }
}
