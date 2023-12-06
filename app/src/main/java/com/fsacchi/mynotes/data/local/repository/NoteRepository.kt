package com.fsacchi.mynotes.data.local.repository

import com.fsacchi.mynotes.data.local.dao.NoteDao
import com.fsacchi.mynotes.data.local.entity.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject;

class NoteRepository@Inject constructor(
    private val noteDao: NoteDao
) : NoteRepositoryInterface {
    override suspend fun getAllNotes(): Flow<List<Note>> = noteDao.getAll()

    override suspend fun getNoteById(noteId: Int): Flow<Note> = noteDao.loadById(noteId)

    override suspend fun insertNote(note: Note): Flow<Unit> = flow {
        noteDao.insert(note)
        emit(Unit)
    }

    override suspend fun deleteNote(note: Note): Flow<Unit> = flow {
        noteDao.delete(note)
        emit(Unit)
    }
}