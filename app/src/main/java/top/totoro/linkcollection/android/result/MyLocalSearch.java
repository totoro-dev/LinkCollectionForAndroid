package top.totoro.linkcollection.android.result;

import entry.CollectionInfo;
import linkcollection.common.interfaces.LocalSearch;
import top.totoro.linkcollection.android.service.SQLService;

public class MyLocalSearch implements LocalSearch {

    @Override
    public void createIndex(CollectionInfo collectionInfo) {
        SQLService.getInstance().createIndex(collectionInfo);
    }

    @Override
    public CollectionInfo[] searchInLocal(String key) {
        return SQLService.getInstance().searchCollectionInfo(key);
    }
}
