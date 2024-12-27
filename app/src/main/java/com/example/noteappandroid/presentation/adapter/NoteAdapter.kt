package com.example.noteappandroid.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappandroid.databinding.NoteItemBinding
import com.example.noteappandroid.domain.model.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = mutableListOf<Note>()
    private var noteClickListener: ((Note) -> Unit)? = null
    private var deleteNoteListener: ((Note) -> Unit)? = null

    fun setNotes(notes: MutableList<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    fun setNoteClickListener(listener: ((Note) -> Unit)) {
        this.noteClickListener = listener
    }

    fun setNoteDeleteListener(listener: ((Note) -> Unit)) {
        this.deleteNoteListener = listener
    }

    inner class NoteViewHolder(val noteItemBinding: NoteItemBinding): RecyclerView.ViewHolder(noteItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteItemBinding.apply {
            noteTitle.text = note.noteTitle
            noteDescription.text = note.noteSubTitle
        }
        holder.noteItemBinding.deleteNote.setOnClickListener {
            deleteNoteListener?.invoke(note)
        }
        holder.noteItemBinding.root.setOnClickListener {
            noteClickListener?.invoke(note)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}