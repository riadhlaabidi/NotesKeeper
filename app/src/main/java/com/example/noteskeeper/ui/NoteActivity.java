package com.example.noteskeeper.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.util.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.noteskeeper.R;
import com.example.noteskeeper.db.entity.NoteEntity;
import com.example.noteskeeper.viewmodel.NoteViewModel;

public class NoteActivity extends AppCompatActivity {

    public static final String KEY_NOTE_ID = "NOTE_ID";
    public static final int ID_NOT_SET = -1;

    private int mNoteId;
    private boolean mIsNewNote;
    private EditText mTitleEditText;
    private EditText mTextEditText;

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleEditText = findViewById(R.id.text_note_title);
        mTextEditText = findViewById(R.id.text_note_text);

        Intent intent = getIntent();
        mNoteId = intent.getIntExtra(KEY_NOTE_ID, ID_NOT_SET);
        mIsNewNote = mNoteId == ID_NOT_SET;

        NoteViewModel.Factory factory = new NoteViewModel.Factory(getApplication(), mNoteId);
        noteViewModel = new ViewModelProvider(this, factory).get(NoteViewModel.class);

        if (!mIsNewNote) {
            subscribeUi(noteViewModel.getNote());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void subscribeUi(LiveData<NoteEntity> observableNote) {
        observableNote.observe(this, note -> {
            mTitleEditText.setText(note.getTitle());
            mTextEditText.setText(note.getText());
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        String noteTitle = mTitleEditText.getText().toString().trim();
        String noteText = mTextEditText.getText().toString().trim();

        NoteEntity note = new NoteEntity();
        note.setTitle(noteTitle);
        note.setText(noteText);

        if (TextUtils.isEmpty(noteTitle) && TextUtils.isEmpty(noteText)) {
            if (!mIsNewNote) {
                note.setId(mNoteId);
                noteViewModel.getNote().removeObservers(this);
                noteViewModel.deleteNote(note);
            }
        } else {
            if (!mIsNewNote) {
                note.setId(mNoteId);
            }
            noteViewModel.insertNote(note);
        }
    }
}

