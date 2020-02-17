package top.totoro.linkcollection.android.result;

import android.os.Message;

import linkcollection.common.interfaces.LoginResult;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.base.Constants;

public class MyLoginResult implements LoginResult {
    @Override
    public void loginError(String error) {
        switch (error) {
            case "请重新登录":
            case "请注册或登录":
            default:
                break;
        }
        Message msg = new Message();
        msg.what = Constants.LOGIN_FAILED;
        msg.obj = error;
        BaseActivity.getInstance().handler.sendMessage(msg);
    }

    @Override
    public void loginSuccess(String s) {
//        MainActionBar.setUserName(Info.getUserName());
//        CollectLabelAdapter.refreshInstance(Info.getCollectionInfos());
//        LoveLabelAdapter.refreshInstance(Info.getLoveInfo());
//        PushAdapter.refreshInstance(Info.getLoveInfo());
//        new RecyclerView(LeftSelectBar.contentPanel).setAdapter(CollectLabelAdapter.getInstance());
//        WidgetConstant.setVisibleSize(800, 600); // 确保更新显示时大小正确
//        MainContentPanel.showPushContent();
//        WidgetConstant.rollBackVisibleSize(); // 更新后回退布局大小为当前显示界面大小
//        new CollectionFrame(MainFrame.context); // 只有登录成功时才会初始化收藏界面，但是否显示由MonitorResult决定
//        Log.d(getClass().getSimpleName(), s);
//        Log.d(getClass().getSimpleName(), Search.searchInLocal("搜索").size() + "");
        BaseActivity.getInstance().handler.sendEmptyMessage(Constants.LOGIN_SUCCESS);
    }
}
