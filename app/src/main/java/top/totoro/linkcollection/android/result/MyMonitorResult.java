package top.totoro.linkcollection.android.result;

import android.os.Message;

import java.util.Arrays;

import entry.CollectionInfo;
import linkcollection.common.interfaces.MonitorResult;
import monitor.Background;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.base.BaseApplication;
import top.totoro.linkcollection.android.base.Constants;

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
        CollectionInfo info = new CollectionInfo(link, "", Arrays.asList(label_1, label_2, label_3).toArray(new String[3]), title);
        Message msg = new Message();
        msg.what = Constants.SPIDER_SUCCESS;
        msg.obj = info;
        BaseActivity.getInstance().handler.sendMessage(msg);
    }

}