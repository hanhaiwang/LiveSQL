package com.hanhaiwang.livesql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hanhaiwang.livesql.core.LiveFactory;

import java.io.File;

/**
 * 提供给用户调用的接口
 */
public class LiveSQL {
    private static final LiveSQL instance = new LiveSQL();
    private static SQLiteDatabase sqLiteDatabase;
    private static String dbPath;

    public static LiveSQL getInstance() {
        return instance;
    }

    public static void init(Context context, String sqlName) {
        dbPath = context.getFilesDir().getPath() + File.separator + sqlName;
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }

    private LiveSQL() {
    }

    public <T> LiveFactory<T> create(Class<T> entityClass) {
        LiveFactory liveFactory = null;
        try {
            liveFactory = LiveFactory.class.newInstance();
            liveFactory.init(sqLiteDatabase, entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liveFactory;
    }
}
