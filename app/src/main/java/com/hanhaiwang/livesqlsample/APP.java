package com.hanhaiwang.livesqlsample;

import android.app.Application;

import com.hanhaiwang.livesql.LiveSQL;

public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LiveSQL.init(this,"live");
    }
}
