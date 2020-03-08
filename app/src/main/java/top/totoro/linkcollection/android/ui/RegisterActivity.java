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
import top.totoro.linkcollection.android.dialog.LoadingDialog;
import top.totoro.linkcollection.android.util.FindView;
import user.Register;

/**
 * Create by HLM on 2020-02-14
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText etNickName;
    private EditText etEmail;
    private EditText etPwd;
    private EditText etRePwd;
    private Button btnRegister;
    private TextView tvGoToLogin;

    private FindView find;

    @Override
    protected void onStart() {
        super.onStart();
        etNickName = find.EditText(R.id.register_nickname);
        etEmail = find.EditText(R.id.register_email);
        etPwd = find.EditText(R.id.register_pwd);
        etRePwd = find.EditText(R.id.register_re_pwd);
        btnRegister = find.Button(R.id.btn_register);
        tvGoToLogin = find.TextView(R.id.from_register_to_login);
        btnRegister.setOnClickListener(this);
        tvGoToLogin.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        find = new FindView(findViewById(R.id.register_layout));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String name = etNickName.getText().toString();
                String email = etEmail.getText().toString();
                String pwd = etPwd.getText().toString();
                String rePwd = etRePwd.getText().toString();
                if ("".equals(name)) {
                    Toast.makeText(this, R.string.name_is_empty, Toast.LENGTH_SHORT).show();
                } else if ("".equals(email)) {
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
                    LoadingDialog.getInstance().show(getSupportFragmentManager()); // 开启加载框
                    new Thread(() -> {
                        Register.register(name, email, pwd);
                    }).start();
                }
                break;
            case R.id.from_register_to_login:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
        }
    }
}
