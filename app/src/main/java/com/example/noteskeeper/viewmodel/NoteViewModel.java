package com.example.noteskeeper.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.noteskeeper.DataRepository;
import com.example.noteskeeper.NotesKeeperApplication;
import com.example.noteskeeper.db.entity.NoteEntity;
import com.example.noteskeeper.model.Note;
import com.example.noteskeeper.ui.NoteActivity;

public class NoteViewModel extends AndroidViewModel {

    private final LiveData<NoteEntity> mObservableNote;
    private final int mNoteId;
    private DataRepository mRepository;

    public NoteViewModel(@NonNull Application application,
                         final int noteId) {
        super(application);
        mNoteId = noteId;
        mRepository = ((NotesKeeperApplication) application).getRepository();
        mObservableNote = mRepository.loadNote(noteId);
    }

    public LiveData<NoteEntity> getNote() {
        return mObservableNote;
    }

    public void insertNote(NoteEntity note) {
        mRepository.insertNote(note);
    }

    public void deleteNote(NoteEntity note) {
        mRepository.deleteNote(note);
    }

    // Factory to inject the noteId dependency into the NoteViewModel construction
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mNoteId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int noteId) {
            mApplication = application;
            mNoteId = noteId;
            mRepository = ((NotesKeeperApplication) application).getRepository();
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new NoteViewModel(mApplication, mNoteId);
        }
    }
}
