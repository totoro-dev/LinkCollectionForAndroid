package top.totoro.linkcollection.android.result;

import android.os.Message;

import linkcollection.common.interfaces.LoginResult;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.base.Constants;

public class MyLoginResult implements LoginResult {
    @Override
    public void autoLoginError(String error) {
        switch (error) {
            case "请重新登录":
            case "请注册或登录":
            default:
                break;
        }
        Message msg = new Message();
        msg.what = Constants.AUTO_LOGIN_FAILED;
        msg.obj = error;
        BaseActivity.getInstance().handler.sendMessage(msg);
    }

    @Override
    public void loginError(String error) {
        Message msg = new Message();
        msg.what = Constants.LOGIN_FAILED;
        msg.obj = error;
        BaseActivity.getInstance().handler.sendMessage(msg);
    }

    @Override
    public void loginSuccess(String s) {
        BaseActivity.getInstance().handler.sendEmptyMessage(Constants.LOGIN_SUCCESS);
    }
}
