package com.example.noteskeeper.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.noteskeeper.db.dao.NoteDao;
import com.example.noteskeeper.db.entity.NoteEntity;
import com.example.noteskeeper.db.entity.NoteFtsEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NoteEntity.class, NoteFtsEntity.class}, version = 2)
public abstract class NotesDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes-keeper-db";

    private static NotesDatabase sInstance;

    private static final int NUMBER_OF_THREADS = 3;
    public static final ExecutorService diskIO = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract NoteDao noteDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static NotesDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (NotesDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private static NotesDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, NotesDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        diskIO.execute(() -> {
                            NotesDatabase database = NotesDatabase.getInstance(appContext);
                            List<NoteEntity> notes = DataGenerator.generateNotes();
                            insertData(database, notes);
                            database.setDatabaseCreated();
                        });
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    private static void insertData(final NotesDatabase database,
                                   final List<NoteEntity> notes) {
        database.runInTransaction(() -> database.noteDao().insertAll(notes));
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `notesFts` USING FTS4("
                    + "`title` TEXT, `text` TEXT, content=`notes`)");
            database.execSQL("INSERT INTO notesFts (`rowid`, `title`, `text`) "
                    + "SELECT `id`, `title`, `text` FROM notes");

        }
    };
}
