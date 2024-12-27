package com.example.noteappandroid.domain.usecase

import com.example.noteappandroid.common.states.NoteFetchState
import com.example.noteappandroid.domain.repository.NoteRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllNotesUsecase @Inject constructor(private val noteRepository: NoteRepository) {
    operator fun invoke() =  flow {
        try {
            emit(NoteFetchState.Loading)
            val response = noteRepository.getAllNotes()
            emit(NoteFetchState.AllNotesFetched(response))
        } catch (e: Exception) {
            emit(NoteFetchState.NoteOperationFailed(e.localizedMessage ?: "Some error occured."))
        }
    }
}