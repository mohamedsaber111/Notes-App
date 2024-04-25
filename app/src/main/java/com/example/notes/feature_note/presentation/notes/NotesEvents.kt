package com.example.notes.feature_note.presentation.notes

import com.example.notes.feature_note.domain.model.Note
import com.example.notes.feature_note.domain.util.NoteOrder

//add events for every single UI action the user can make, we have an event
sealed class NotesEvent{
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note :Note): NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
}
