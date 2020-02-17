package top.totoro.linkcollection.android.result;

import linkcollection.common.interfaces.UserResult;

public class MyUserResult implements UserResult {
    @Override
    public void loginResult(String s) {
//        String userId = s;
//        if (userId == null) AppCommon.getLoginResult().loginError("网络错误");
//        switch (userId) {
//            case "用户不存在":
//            case "密码错误":
//                AppCommon.getLoginResult().loginError(userId);
//                break;
//            default:
//                TFile.builder().toDisk(Disk.TMP).toPath(Constans.INFO_PATH).toName(Constans.LOGIN_INFO_FILE_NAME).toFile();
//                if (!TFile.getProperty().exists()) TFile.builder().create();
//                TWriter writer = new TWriter(TFile.getProperty());
//                String[] info = UserInfoTool.get(Login.getName());
//                String loginInfo = "{\"headId\":\"" + info[0] + "\",\"tailId\":\"" + info[1] + "\",\"name\":\"" + Login.getName() + "\"}";
//                writer.write(loginInfo);
//                TFile.builder().recycle();
//                // 处理用户链接收集信息
//                Info.refreshCollectionInfo(userId);
//                AppCommon.getLoginResult().loginSuccess(userId);
//                break;
//        }
    }

    @Override
    public void registerResult(String s) {

    }

    @Override
    public void checkEmailResult(String s) {

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

    }

    @Override
    public void updateVip(String s) {

    }
}
