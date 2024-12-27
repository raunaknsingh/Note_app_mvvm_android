package com.example.noteappandroid.domain.usecase

import com.example.noteappandroid.common.states.NoteUpdationState
import com.example.noteappandroid.domain.model.Note
import com.example.noteappandroid.domain.repository.NoteRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateNoteUsecase @Inject constructor(private val noteRepository: NoteRepository) {
    operator fun invoke(note: Note) =  flow {
        try {
            emit(NoteUpdationState.Loading)
            val response = noteRepository.updateNote(note)
            if (response == 0) {
                emit(NoteUpdationState.NoteUpdationFailed("No note was updated"))
            } else {
                emit(NoteUpdationState.NoteUpdated("$response note was updated"))
            }
        } catch (e: Exception) {
            emit(NoteUpdationState.NoteOperationFailed(e.localizedMessage ?: "Some error occured."))
        }
    }
}