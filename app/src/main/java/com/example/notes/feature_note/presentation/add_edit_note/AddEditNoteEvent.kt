package com.example.notes.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

//add events for every single UI action the user can make, we have an event
sealed class AddEditNoteEvent{

    data class EnteredTitle(val value: String): AddEditNoteEvent()
    //want to hide the hint when focus in title or content
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()

    data class EnteredContent(val value: String): AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditNoteEvent()

    data class ChangeColor(val color: Int): AddEditNoteEvent()

    object SaveNote: AddEditNoteEvent()
}
