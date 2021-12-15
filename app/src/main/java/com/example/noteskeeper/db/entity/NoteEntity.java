package com.example.noteskeeper.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.noteskeeper.model.Note;

@Entity(tableName = "notes")
public class NoteEntity implements Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String text;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NoteEntity() { }

    @Ignore
    public NoteEntity(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public NoteEntity(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.text = note.getText();
    }
}
