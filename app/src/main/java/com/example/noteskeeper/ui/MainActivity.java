package com.example.noteskeeper.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteskeeper.R;
import com.example.noteskeeper.db.entity.NoteEntity;
import com.example.noteskeeper.viewmodel.NotesListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteRecyclerAdapter mNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            startActivity(intent);
        });

        RecyclerView mRecyclerNotes = findViewById(R.id.notes_list);

        mNoteAdapter = new NoteRecyclerAdapter(this);
        final StaggeredGridLayoutManager notesLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerNotes.setLayoutManager(notesLayoutManager);
        mRecyclerNotes.setAdapter(mNoteAdapter);

        NotesListViewModel viewModel = new ViewModelProvider(this).get(NotesListViewModel.class);

        ImageButton searchBtn = findViewById(R.id.notes_search_btn);
        searchBtn.setOnClickListener(event -> {
            EditText query = findViewById(R.id.notes_search_box);
            viewModel.setQuery(query.getText());
        });

        subscribeUi(viewModel.getNotes());
    }

    private void subscribeUi(LiveData<List<NoteEntity>> liveData) {
        liveData.observe(this, notes -> mNoteAdapter.setNoteList(notes));
    }

}