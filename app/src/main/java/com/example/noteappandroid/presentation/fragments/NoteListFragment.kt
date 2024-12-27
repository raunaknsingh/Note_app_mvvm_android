package com.example.noteappandroid.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.noteappandroid.common.extension.showToast
import com.example.noteappandroid.databinding.FragmentNoteListBinding
import com.example.noteappandroid.domain.model.Note
import com.example.noteappandroid.presentation.adapter.NoteAdapter
import com.example.noteappandroid.presentation.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private lateinit var _binding : FragmentNoteListBinding

    private val noteViewModel: NoteViewModel by viewModels()
    private var noteAdapter = NoteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.recyclerViewNote.adapter = noteAdapter
        collectData()
        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        fetchNotes()
    }

    private fun setClickListener() {
        _binding.noteAddBtn.setOnClickListener {
            findNavController().navigate(
                NoteListFragmentDirections.actionNoteListFragmentToNoteAddEditFragment(
                    Note(0, "", "")
                )
            )
        }
        noteAdapter.setNoteClickListener {
            findNavController().navigate(
                NoteListFragmentDirections.actionNoteListFragmentToNoteAddEditFragment(
                    it
                )
            )
        }
        noteAdapter.setNoteDeleteListener {
            noteViewModel.deleteNote(it)
        }
    }

    private fun collectData() {
        _binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    launch {
                        noteViewModel.isLoading.collect {
                            progressCircular.visibility = if (it) View.VISIBLE else View.GONE
                        }
                    }

                    launch {
                        noteViewModel.noteInfo.collect {
                            context?.showToast(it)
                        }
                    }

                    launch {
                        noteViewModel.notes.collect {
                            noteAdapter.setNotes(it.toMutableList())
                        }
                    }

                    launch {
                        noteViewModel.isNoteOperationSuccessful.collect {
                            if (it) {
                                fetchNotes()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fetchNotes() {
        noteViewModel.getAllNotes()
    }
}