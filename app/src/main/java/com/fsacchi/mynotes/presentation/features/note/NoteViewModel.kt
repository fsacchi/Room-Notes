package com.fsacchi.mynotes.presentation.features.note

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.mynotes.data.local.entity.Note
import com.fsacchi.mynotes.data.local.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    val allNotes: MutableState<List<Note>?> = mutableStateOf(null)
    val note: MutableState<Note?> = mutableStateOf(null)
    val insertState: MutableState<Unit?> = mutableStateOf(null)

    fun getAllNotes() {
        viewModelScope.launch {
            noteRepository.getAllNotes().collect {
                allNotes.value = it
            }
        }
    }

    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            noteRepository.getNoteById(noteId).collect {
                note.value = it
            }
        }
    }

    fun update(note: Note) {
        viewModelScope.launch {
            noteRepository.insertNote(note).collect{
                insertState.value = it
            }
        }
    }

    fun insert(title: String, note: String) {
        viewModelScope.launch {
            noteRepository.insertNote(Note(title = title, note = note)).collect{
                insertState.value = it
            }
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note).collect{
                getAllNotes()
            }
        }
    }
}