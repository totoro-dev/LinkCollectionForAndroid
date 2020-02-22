package top.totoro.linkcollection.android.result;

import linkcollection.common.interfaces.CheckMailResult;

public class MyCheckMailResult implements CheckMailResult {
    @Override
    public void checkEmailError(String s) {
//        Message msg = new Message();
//        msg.what = Constants.CHECK_MAIL_FAILED;
//        msg.obj = s;
//        BaseActivity.getInstance().handler.sendMessage(msg);
    }

    @Override
    public void checkEmailSuccess() {
//        Logger.d(this,"check mail success");
//        BaseActivity.getInstance().handler.sendEmptyMessage(Constants.CHECK_MAIL_SUCCESS);
    }
}
