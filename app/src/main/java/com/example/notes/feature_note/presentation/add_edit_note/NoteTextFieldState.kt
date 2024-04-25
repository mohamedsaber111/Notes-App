package com.example.notes.feature_note.presentation.add_edit_note

//to handle hint text for each title and content and the hint text visibility
data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)