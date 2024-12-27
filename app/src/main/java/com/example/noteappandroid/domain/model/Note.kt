package com.example.noteappandroid.domain.model

import android.os.Parcelable
import com.example.noteappandroid.data.local.NoteEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val noteId: Int,
    val noteTitle: String,
    val noteSubTitle: String,
): Parcelable

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        noteId = this.noteId,
        noteTitle = this.noteTitle,
        noteSubTitle = this.noteSubTitle
    )
}
