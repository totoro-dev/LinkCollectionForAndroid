package top.totoro.linkcollection.androidcommon.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import entry.CollectionInfo;
import top.totoro.linkcollection.androidcommon.base.BaseApplication;

/**
 * 处理用户收藏链接的数据库
 * Create by HLM on 2020-02-14
 */
public class SQLService extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "LinkCollect";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            "linkId text primary key," +
            "link text," +
            "title text," +
            "labels text)";

    private static SQLiteDatabase db;

    private static SQLService instance;

    public static SQLService getInstance() {
        synchronized (SQLService.class) {
            if (instance == null) {
                instance = new SQLService(BaseApplication.getContext(), TABLE_NAME, null, 1);
                db = instance.getWritableDatabase();
            }
        }
        return instance;
    }

    private SQLService(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private boolean exist(CollectionInfo collectionInfo) {
        if (collectionInfo.getLinkId() == null) return false;
        Cursor cursor = db.query("LinkCollect", new String[]{"linkId"}, "linkId=" + collectionInfo.getLinkId(), null, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    public void createIndex(CollectionInfo collectionInfo) {
        if (exist(collectionInfo)) return;
        ContentValues values = new ContentValues();
        values.put("linkId", collectionInfo.getLinkId());
        values.put("link", collectionInfo.getLink());
        values.put("title", collectionInfo.getTitle());
        values.put("labels", String.join(",", collectionInfo.getLabels()));
        db.insert("LinkCollect", null, values);
    }

    public CollectionInfo[] searchCollectionInfo(String key) {
        Cursor cursor = db.query(true, TABLE_NAME, new String[]{"linkId", "link", "title", "labels"}, "title like '%" + key + "%' or labels like '%" + key + "%'", null, null, null, null, null);
        int index = 0;
        CollectionInfo[] result = new CollectionInfo[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                String linkId = cursor.getString(0);
                String link = cursor.getString(1);
                String title = cursor.getString(2);
                String labels = cursor.getString(3);
                CollectionInfo info = new CollectionInfo(link, linkId, labels.split(","), title);
                result[index] = info;
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
}
