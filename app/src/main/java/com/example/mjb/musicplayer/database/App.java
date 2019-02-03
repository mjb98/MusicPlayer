package com.example.mjb.musicplayer.database;

import android.app.Application;

import com.example.mjb.musicplayer.model.DaoMaster;
import com.example.mjb.musicplayer.model.DaoSession;

import org.greenrobot.greendao.database.Database;

public class App extends Application {

    private static App app;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        MyDevOpenHelper myDevOpenHelper = new MyDevOpenHelper(this, "DatabaseName");
        Database db = myDevOpenHelper.getWritableDb();

        daoSession = new DaoMaster(db).newSession();

        app = this;
    }

    public static App getApp() {
        return app;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}