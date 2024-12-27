package com.example.noteappandroid.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappandroid.common.states.NoteDeletionState
import com.example.noteappandroid.common.states.NoteFetchState
import com.example.noteappandroid.common.states.NoteInsertionState
import com.example.noteappandroid.common.states.NoteUpdationState
import com.example.noteappandroid.domain.model.Note
import com.example.noteappandroid.domain.usecase.DeleteNoteUsecase
import com.example.noteappandroid.domain.usecase.GetAllNotesUsecase
import com.example.noteappandroid.domain.usecase.InsertNoteUsecase
import com.example.noteappandroid.domain.usecase.UpdateNoteUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val insertNoteUsecase: InsertNoteUsecase,
    private val updateNoteUsecase: UpdateNoteUsecase,
    private val deleteNoteUsecase: DeleteNoteUsecase,
    private val getAllNotesUsecase: GetAllNotesUsecase
): ViewModel() {

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _notes = MutableSharedFlow<List<Note>>()
    val notes: SharedFlow<List<Note>> = _notes

    private var _noteInfo = MutableSharedFlow<String>()
    val noteInfo: SharedFlow<String> = _noteInfo

    private var _isNoteOperationSuccessful = MutableSharedFlow<Boolean>()
    val isNoteOperationSuccessful: SharedFlow<Boolean> = _isNoteOperationSuccessful

    fun insertNote(note: Note) {
        viewModelScope.launch {
            insertNoteUsecase(note).collect {
                when (it) {
                    is NoteInsertionState.Loading -> showProgress()
                    is NoteInsertionState.NoteInsertionFailed -> {
                        hideProgress()
                        showToast(it.error)
                        updateNoteOperationSuccess(false)
                    }
                    is NoteInsertionState.NoteOperationFailed -> {
                        hideProgress()
                        showToast(it.error)
                        updateNoteOperationSuccess(false)
                    }
                    is NoteInsertionState.NoteInserted -> {
                        hideProgress()
                        showToast(it.noteRecordId)
                        updateNoteOperationSuccess(true)
                    }
                }
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            updateNoteUsecase(note).collect {
                when (it) {
                    is NoteUpdationState.Loading -> showProgress()
                    is NoteUpdationState.NoteUpdationFailed -> {
                        hideProgress()
                        showToast(it.error)
                        updateNoteOperationSuccess(false)
                    }
                    is NoteUpdationState.NoteOperationFailed -> {
                        hideProgress()
                        showToast(it.error)
                        updateNoteOperationSuccess(false)
                    }
                    is NoteUpdationState.NoteUpdated -> {
                        hideProgress()
                        showToast(it.noOfRowsUpdated)
                        updateNoteOperationSuccess(true)
                    }
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUsecase(note).collect {
                when (it) {
                    is NoteDeletionState.Loading -> showProgress()
                    is NoteDeletionState.NoteDeletionFailed -> {
                        hideProgress()
                        showToast(it.error)
                        updateNoteOperationSuccess(false)
                    }
                    is NoteDeletionState.NoteOperationFailed -> {
                        hideProgress()
                        showToast(it.error)
                        updateNoteOperationSuccess(false)
                    }
                    is NoteDeletionState.NoteDeleted -> {
                        hideProgress()
                        showToast(it.noOfRowsUpdated)
                        updateNoteOperationSuccess(true)
                    }
                }
            }
        }
    }

    private fun updateNoteOperationSuccess(isNoteOperationSuccessful: Boolean) = viewModelScope.launch {
        _isNoteOperationSuccessful.emit(isNoteOperationSuccessful)
    }

    fun getAllNotes() {
        viewModelScope.launch {
            getAllNotesUsecase().collect {
                when (it) {
                    is NoteFetchState.Loading -> showProgress()
                    is NoteFetchState.AllNotesFetchFailed -> {
                        hideProgress()
                        showToast(it.error)
                    }
                    is NoteFetchState.NoteOperationFailed -> {
                        hideProgress()
                        showToast(it.error)
                    }
                    is NoteFetchState.AllNotesFetched -> {
                        hideProgress()
                        showNotesList(it.notes)
                    }
                }
            }
        }
    }

    private fun showNotesList(notes: List<Note>) = viewModelScope.launch {
        _notes.emit(notes)
    }

    private fun showToast(noteInfo: String) = viewModelScope.launch {
        _noteInfo.emit(noteInfo)
    }

    private fun hideProgress() = viewModelScope.launch {
        _isLoading.value = false
    }

    private fun showProgress() = viewModelScope.launch {
        _isLoading.value = true
    }
}