package com.example.noteskeeper.db.entity;

import androidx.room.Entity;
import androidx.room.Fts4;

@Entity(tableName = "notesFts")
@Fts4(contentEntity = NoteEntity.class)
public class NoteFtsEntity {

    private String title;
    private String text;

    public NoteFtsEntity(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}