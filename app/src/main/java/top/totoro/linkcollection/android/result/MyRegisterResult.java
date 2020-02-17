package top.totoro.linkcollection.android.result;

import android.os.Message;

import linkcollection.common.interfaces.RegisterResult;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.base.Constants;

public class MyRegisterResult implements RegisterResult {
    @Override
    public void registerError(String s) {
        Message msg = new Message();
        msg.what = Constants.REGISTER_FAILED;
        msg.obj = s;
        BaseActivity.getInstance().handler.sendMessage(msg);
    }

    @Override
    public void registerSuccess() {
        BaseActivity.getInstance().handler.sendEmptyMessage(Constants.REGISTER_SUCCESS);
    }
}
