package top.totoro.linkcollection.android.result;

import android.os.Message;

import linkcollection.common.interfaces.CheckMailResult;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.base.Constants;

public class MyCheckMailResult implements CheckMailResult {
    @Override
    public void checkEmailError(String s) {
        Message msg = new Message();
        msg.what = Constants.CHECK_MAIL_FAILED;
        msg.obj = s;
        BaseActivity.getInstance().handler.sendMessage(msg);
    }

    @Override
    public void checkEmailSuccess() {
        BaseActivity.getInstance().handler.sendEmptyMessage(Constants.CHECK_MAIL_SUCCESS);
    }
}
