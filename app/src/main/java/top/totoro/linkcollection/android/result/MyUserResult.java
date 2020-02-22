package top.totoro.linkcollection.android.result;

import android.os.Message;

import linkcollection.common.interfaces.UserResult;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.base.Constants;
import top.totoro.linkcollection.android.util.Logger;

public class MyUserResult implements UserResult {
    @Override
    public void loginResult(String s) {
    }

    @Override
    public void registerResult(String s) {

    }

    @Override
    public void checkEmailResult(String s) {
        Logger.d(this, "check mail result : " + s);
        Message msg = new Message();
        if ("验证成功".equals(s)) {
            msg.what = Constants.CHECK_MAIL_SUCCESS;
        } else {
            msg.what = Constants.CHECK_MAIL_FAILED;
        }
        msg.obj = s;
        BaseActivity.getInstance().handler.sendMessage(msg);
    }

    @Override
    public void getUserInfo(String s) {

    }

    @Override
    public void updateCollections(String s) {

    }

    @Override
    public void updateLikes(String s) {

    }

    @Override
    public void updateLoves(String s) {

    }

    @Override
    public void updatePwd(String s) {
        Message msg = new Message();
        msg.what = Constants.UPDATE_PWD_SEND_CODE;
        msg.obj = s;
        BaseActivity.getInstance().handler.sendMessage(msg);
    }

    @Override
    public void updateVip(String s) {

    }
}
