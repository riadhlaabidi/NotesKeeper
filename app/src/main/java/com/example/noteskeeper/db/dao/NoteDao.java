package com.example.noteskeeper.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.noteskeeper.db.entity.NoteEntity;

import java.util.List;

@Dao
public interface NoteDao {

    @Delete
    void delete(NoteEntity note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NoteEntity note);

    @Insert
    void insertAll(List<NoteEntity> notes);

    @Query("select * from notes where id = :noteId")
    LiveData<NoteEntity> loadNote(int noteId);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    LiveData<List<NoteEntity>> loadAllNotes();

    @Query("SELECT notes.* FROM notes " +
            "JOIN notesFts ON (notes.id = notesFts.rowid) " +
            "WHERE notesFts MATCH :query " +
            "ORDER BY id DESC")
    LiveData<List<NoteEntity>> searchAllNotes(String query);
}
