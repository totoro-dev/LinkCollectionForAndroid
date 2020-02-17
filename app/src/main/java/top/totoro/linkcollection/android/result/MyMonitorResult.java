package top.totoro.linkcollection.android.result;

import android.os.Message;

import linkcollection.common.interfaces.MonitorResult;
import monitor.Background;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.base.BaseApplication;
import top.totoro.linkcollection.android.base.Constants;
import top.totoro.linkcollection.android.util.Logger;

public class MyMonitorResult implements MonitorResult {

    @Override
    public String getClipboardContent() {
        return BaseApplication.getInstance().getClipboardContent();
    }

    @Override
    public void clipboardContentChanged() {
        Background.startService();
    }

    @Override
    public void spiderSuccess(String link, String title, String label_1, String label_2, String label_3) {
        Logger.d(this, link, title, label_1);
        Message msg = new Message();
        msg.what = Constants.SPIDER_SUCCESS;
        msg.obj = link + "," + title + "," + label_1 + "," + label_2 + "," + label_3;
        BaseActivity.getInstance().handler.sendMessage(msg);
    }

}