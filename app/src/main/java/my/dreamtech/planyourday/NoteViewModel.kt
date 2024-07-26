package my.dreamtech.planyourday

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import my.dreamtech.planyourday.data.Graph
import my.dreamtech.planyourday.notedata.NoteData
import my.dreamtech.planyourday.notedata.NoteRepository

class NoteViewModel(
    private val noteRepository: NoteRepository = Graph.noteRepository
) : ViewModel() {

    var noteDescriptionState by mutableStateOf("")

    var noteTitleState by mutableStateOf("")

    fun onNoteDescChanged(newString : String){
        noteDescriptionState = newString
    }

    fun onNoteTitleChanged(newString : String){
        noteTitleState = newString
    }


    lateinit var getAllNotes: Flow<List<NoteData>>

    init {
        viewModelScope.launch {
            getAllNotes = noteRepository.getAllNotes()
        }
    }

    fun addNote(noteData: NoteData){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.addNote(noteData)
        }
    }

    fun getNoteById(id: Long) : Flow<NoteData> {
        return noteRepository.getNoteById(id)
    }

    fun updateNote(noteData: NoteData){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(noteData)
        }
    }

    fun deleteNote(noteData: NoteData){
        viewModelScope.launch (Dispatchers.IO){
            noteRepository.deleteNote(noteData)
            getAllNotes = noteRepository.getAllNotes()
        }
    }

}