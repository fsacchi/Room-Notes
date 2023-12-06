package com.fsacchi.mynotes.data.local.repository

import com.fsacchi.mynotes.data.local.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepositoryInterface {
    suspend fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(noteId: Int): Flow<Note>
    suspend fun insertNote(note: Note): Flow<Unit>
    suspend fun deleteNote(note: Note): Flow<Unit>
}