package com.fsacchi.mynotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fsacchi.mynotes.data.local.entity.Note
import com.fsacchi.mynotes.data.local.dao.NoteDao

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
