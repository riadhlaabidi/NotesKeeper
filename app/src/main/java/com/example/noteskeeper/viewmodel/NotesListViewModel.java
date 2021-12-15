package com.example.noteskeeper.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import com.example.noteskeeper.DataRepository;
import com.example.noteskeeper.NotesKeeperApplication;
import com.example.noteskeeper.db.entity.NoteEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotesListViewModel extends AndroidViewModel {

    private static final String QUERY_KEY = "QUERY";

    private final SavedStateHandle mSavedStateHandler;
    private DataRepository mRepository;
    private LiveData<List<NoteEntity>> mNotes;

    public NotesListViewModel(@NonNull Application application,
                              @NotNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandler = savedStateHandle;
        mRepository = ((NotesKeeperApplication) application).getRepository();
        mNotes = Transformations.switchMap(
                savedStateHandle.getLiveData(QUERY_KEY, null),
                (Function<CharSequence, LiveData<List<NoteEntity>>>) query -> {
                    if (TextUtils.isEmpty(query)) {
                        return mRepository.getNotes();
                    }
                    return mRepository.searchNotes("*" + query + "*");
                });
    }

    public void setQuery(CharSequence query) {
        mSavedStateHandler.set(QUERY_KEY, query);
    }

    public LiveData<List<NoteEntity>> getNotes() {
        return mNotes;
    }
}
