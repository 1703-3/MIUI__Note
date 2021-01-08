package net.micode.notes;

import android.app.Application;

import org.litepal.LitePal;

public class NoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
