package com.example.notes.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.feature_note.domain.model.Note
import com.example.notes.feature_note.domain.use_case.NoteUseCases
import com.example.notes.feature_note.domain.util.NoteOrder
import com.example.notes.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {


    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null


    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event : NotesEvent){
        when(event){
            is NotesEvent.Order ->{
                //if click multi time on the same radio button  that already checked we don't want to do anything
                //noteOrder::class >> compare each classes Color , date , title
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote ->{
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote ->{
                viewModelScope.launch {
                    //?: return@launch -> if null return launch which shouldn't actually happen
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    //so if RestoreNote called multiple time from ui then wouldn't insert same note again
                    recentlyDeletedNote=null
                }
            }
            is NotesEvent.ToggleOrderSection ->{
                _state.value = state.value.copy(
                    //! -> is toggled
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

        }
    }
    private fun getNotes(noteOrder: NoteOrder) {
        //every time we call getNotes() we will get new instance of that flow
        // we want when recall this fun cancel old coroutine that is observing our database
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}