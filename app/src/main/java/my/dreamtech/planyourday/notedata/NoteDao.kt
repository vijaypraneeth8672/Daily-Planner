package my.dreamtech.planyourday.notedata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow



@Dao
abstract class NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addNote(noteEntity: NoteData)

    @Query("Select * from 'notes_table'")
    abstract  fun getAllNotes() : Flow<List<NoteData>>

    @Update
    abstract suspend fun updateNote(noteEntity: NoteData)

    @Delete
    abstract suspend fun deleteNote(noteEntity: NoteData)

    @Query("Select * from 'notes_table' where id =:id")
    abstract fun getNoteById(id:Long) : Flow<NoteData>
}