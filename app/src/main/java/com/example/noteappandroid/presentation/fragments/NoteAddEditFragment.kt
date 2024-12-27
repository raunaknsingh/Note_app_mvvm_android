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
import androidx.navigation.fragment.navArgs
import com.example.noteappandroid.common.extension.showToast
import com.example.noteappandroid.databinding.FragmentNoteAddEditBinding
import com.example.noteappandroid.domain.model.Note
import com.example.noteappandroid.presentation.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteAddEditFragment : Fragment() {

    private lateinit var _binding : FragmentNoteAddEditBinding

    private val noteViewModel: NoteViewModel by viewModels()

    private val args: NoteAddEditFragmentArgs by navArgs()
    private var isAdd = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNoteAddEditBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.Note.let {
            _binding.apply {
                noteTitle.setText(it.noteTitle)
                noteDescription.setText(it.noteSubTitle)
                if (it.noteId != 0) {
                    saveButton.text = "Update Note"
                    isAdd = false
                }
            }
        }

        setClickListener()
        collectData()
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
                        noteViewModel.isNoteOperationSuccessful.collect {
                            if (it) {
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setClickListener() {
        with(_binding) {
            saveButton.setOnClickListener {
                if (isAdd) {
                    noteViewModel.insertNote(
                        Note(
                            noteId = 0,
                            noteTitle = noteTitle.text.trim().toString(),
                            noteSubTitle = noteDescription.text.trim().toString(),
                        )
                    )
                } else {
                    noteViewModel.updateNote(
                        Note(
                            noteId = args.Note.noteId,
                            noteTitle = noteTitle.text.trim().toString(),
                            noteSubTitle = noteDescription.text.trim().toString(),
                        )
                    )
                }
            }
        }

    }
}