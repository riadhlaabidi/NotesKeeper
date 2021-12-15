package com.example.noteskeeper;

import android.app.Application;

import com.example.noteskeeper.db.NotesDatabase;

public class NotesKeeperApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public NotesDatabase getDatabase() {
        return NotesDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(this);
    }
}
