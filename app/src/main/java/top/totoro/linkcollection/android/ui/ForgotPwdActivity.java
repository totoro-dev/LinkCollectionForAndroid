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
import user.Info;

/**
 * Create by HLM on 2020-02-22
 */
public class ForgotPwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPwd;
    private EditText etRePwd;
    private Button btnUpdate;
    private TextView tvGoToLogin;

    private FindView find;

    @Override
    protected void onStart() {
        super.onStart();
        etEmail = find.EditText(R.id.update_pwd_email);
        etPwd = find.EditText(R.id.update_pwd_pwd);
        etRePwd = find.EditText(R.id.update_pwd_re_pwd);
        btnUpdate = find.Button(R.id.btn_update_pwd);
        tvGoToLogin = find.TextView(R.id.from_forgot_pwd_to_login);
        btnUpdate.setOnClickListener(this);
        tvGoToLogin.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        find = new FindView(findViewById(R.id.forgot_pwd_layout));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_pwd:
                String email = etEmail.getText().toString();
                String pwd = etPwd.getText().toString();
                String rePwd = etRePwd.getText().toString();
                if ("".equals(email)) {
                    Toast.makeText(this, R.string.email_is_empty, Toast.LENGTH_SHORT).show();
                } else if (!email.contains("@") || !email.contains(".")) {
                    Toast.makeText(this, R.string.email_format_error, Toast.LENGTH_SHORT).show();
                } else if ("".equals(pwd)) {
                    Toast.makeText(this, R.string.pwd_is_empty, Toast.LENGTH_SHORT).show();
                } else if (pwd.length() < 6) {
                    Toast.makeText(this, R.string.pwd_short_length, Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(rePwd)) {
                    Toast.makeText(this, R.string.pwd_not_same, Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> {
                        Info.updatePwd(email, pwd);
                    }).start();
                }
                break;
            case R.id.from_forgot_pwd_to_login:
                startActivity(new Intent(ForgotPwdActivity.this, LoginActivity.class));
                break;
        }
    }
}
