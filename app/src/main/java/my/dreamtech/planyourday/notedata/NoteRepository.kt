package my.dreamtech.planyourday.notedata

import kotlinx.coroutines.flow.Flow


class NoteRepository(private val noteDao: NoteDao) {
    suspend fun addNote(note: NoteData){
        noteDao.addNote(note)
    }

    fun getAllNotes(): Flow<List<NoteData>> = noteDao.getAllNotes()

    fun getNoteById(id:Long) : Flow<NoteData> {
        return noteDao.getNoteById(id)
    }

    suspend fun updateNote(note: NoteData){
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteData){
        noteDao.deleteNote(note)
    }
}