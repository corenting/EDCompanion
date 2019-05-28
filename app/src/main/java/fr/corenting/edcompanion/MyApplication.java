package fr.corenting.edcompanion;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
