package com.example.noteappandroid.data.repository

import com.example.noteappandroid.data.local.NoteDao
import com.example.noteappandroid.data.local.toNote
import com.example.noteappandroid.domain.model.Note
import com.example.noteappandroid.domain.model.toNoteEntity
import com.example.noteappandroid.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : NoteRepository {

    override suspend fun insertNote(note: Note) = noteDao.insertNote(note.toNoteEntity())

    override suspend fun updateNote(note: Note) = noteDao.updateNote(note.toNoteEntity())

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note.toNoteEntity())

    override suspend fun getAllNotes() = noteDao.getAllNotes().map { it.toNote() }
}