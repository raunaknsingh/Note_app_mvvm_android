package com.example.noteappandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteWrapper(
    val note: Note?
): Parcelable
