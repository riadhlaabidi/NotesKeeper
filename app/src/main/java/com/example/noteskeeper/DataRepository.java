package com.example.noteskeeper;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.noteskeeper.db.NotesDatabase;
import com.example.noteskeeper.db.entity.NoteEntity;

import java.util.List;

public class DataRepository {

    private static DataRepository sInstance;
    private final NotesDatabase mDatabase;
    private MediatorLiveData<List<NoteEntity>> mObservableNotes;

    private DataRepository(Application application) {
        mDatabase = ((NotesKeeperApplication) application).getDatabase();
        mObservableNotes = new MediatorLiveData<>();
        mObservableNotes.addSource(mDatabase.noteDao().loadAllNotes(),
                noteEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableNotes.postValue(noteEntities);
                    }
                });
    }

    public static DataRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(application);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<NoteEntity>> getNotes() {
        return mObservableNotes;
    }

    public LiveData<NoteEntity> loadNote(final int noteId) {
        return mDatabase.noteDao().loadNote(noteId);
    }

    public LiveData<List<NoteEntity>> searchNotes(String query) {
        return mDatabase.noteDao().searchAllNotes(query);
    }

    public void insertNote(NoteEntity note) {
        NotesDatabase.diskIO.execute(() -> mDatabase.noteDao().insert(note));
    }

    public void deleteNote(NoteEntity note) {
        NotesDatabase.diskIO.execute(() -> mDatabase.noteDao().delete(note));
    }
}
