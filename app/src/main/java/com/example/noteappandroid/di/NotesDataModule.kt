package com.example.noteappandroid.di

import android.content.Context
import com.example.noteappandroid.data.local.NoteDao
import com.example.noteappandroid.data.local.NoteDatabase
import com.example.noteappandroid.data.repository.NoteRepositoryImpl
import com.example.noteappandroid.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NotesDataModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context) : NoteDatabase {
        return NoteDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.noteDao()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }
}