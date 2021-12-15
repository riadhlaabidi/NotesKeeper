package com.example.noteskeeper.db;

import com.example.noteskeeper.db.entity.NoteEntity;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    private static final String[] TITLE = new String[]{
            "Note title 1",
            "Note title 2",
            "Note title 3",
            "Note title 4",
            "Note title 5",
            "Note title 6"
    };
    private static final String[] TEXT = new String[]{
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            "Pellentesque ultrices mi ut lorem convallis, non egestas est finibus.",
            "Quisque consectetur erat id ultricies vulputate. Cras quis facilisis ex, id rhoncus velit.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            "Pellentesque ultrices mi ut lorem convallis, non egestas est finibus.",
            "Quisque consectetur erat id ultricies vulputate. Cras quis facilisis ex, id rhoncus velit."
    };

    public static List<NoteEntity> generateNotes() {
        List<NoteEntity> notes = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {
            NoteEntity note = new NoteEntity();
            note.setTitle(TITLE[i]);
            note.setText(TEXT[i]);
            notes.add(note);
        }
        return notes;
    }

}
