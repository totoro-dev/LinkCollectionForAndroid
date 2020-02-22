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
import user.Login;

/**
 * Create by HLM on 2020-02-14
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etPwd;
    private Button btnLogin;
    private TextView tvForgotPwd;
    private TextView tvRegisterAccount;
    private FindView find;

    @Override
    protected void onStart() {
        super.onStart();
        etName = find.EditText(R.id.login_name);
        etPwd = find.EditText(R.id.login_pwd);
        btnLogin = find.Button(R.id.btn_login);
        tvForgotPwd = find.TextView(R.id.forgot_pwd);
        tvRegisterAccount = find.TextView(R.id.register_account);
        btnLogin.setOnClickListener(this);
        tvForgotPwd.setOnClickListener(this);
        tvRegisterAccount.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        find = new FindView(findViewById(R.id.login_layout));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String name = etName.getText().toString();
                String pwd = etPwd.getText().toString();
                if ("".equals(name)) {
                    Toast.makeText(this, R.string.name_is_empty, Toast.LENGTH_SHORT).show();
                } else if ("".equals(pwd)) {
                    Toast.makeText(this, R.string.pwd_is_empty, Toast.LENGTH_SHORT).show();
                } else if (pwd.length() < 6) {
                    Toast.makeText(this, R.string.pwd_short_length, Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> {
                        Login.firstLogin(name, pwd);
                    }).start();
                }
                break;
            case R.id.register_account:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.forgot_pwd:
                startActivity(ForgotPwdActivity.class);
                break;
        }
    }
}
