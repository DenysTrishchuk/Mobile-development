package ua.kpi.comsys.iv8127.android_prog.ui.lab7;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import ua.kpi.comsys.iv8127.android_prog.ui.lab7.AppDatabase;

public class App extends Application {

    public static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;

        database = Room.databaseBuilder(this, AppDatabase.class, "database").build();

        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
