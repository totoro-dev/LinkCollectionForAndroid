package top.totoro.linkcollection.android.result;

import entry.CollectionInfo;
import linkcollection.common.interfaces.LocalSearch;
import top.totoro.linkcollection.android.service.SQLService;
import top.totoro.linkcollection.android.util.Logger;
import user.Login;

public class MyLocalSearch implements LocalSearch {

    @Override
    public void createIndex(CollectionInfo collectionInfo) {
        SQLService.getInstance(Long.parseLong(Login.getUserId())).createIndex(collectionInfo);
    }

    @Override
    public CollectionInfo[] searchInLocal(String key) {
        return SQLService.getInstance(Long.parseLong(Login.getUserId())).searchCollectionInfo(key);
    }

    @Override
    public boolean deleteCollection(String linkId) {
        boolean deleted = SQLService.getInstance(Long.parseLong(Login.getUserId())).deleteCollection(linkId);
        Logger.d(this, "delete result = " + deleted);
        return deleted;
    }
}
