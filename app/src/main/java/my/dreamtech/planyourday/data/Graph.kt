package my.dreamtech.planyourday.data

import android.content.Context
import androidx.room.Room
import my.dreamtech.planyourday.notedata.NoteDatabase
import my.dreamtech.planyourday.notedata.NoteRepository

object Graph  {

    lateinit var database: DataDatabase

    val dataRepository by lazy {
        DataRepository(database.todoDao())
    }

    lateinit var noteDatabase: NoteDatabase

    val noteRepository by lazy {
        NoteRepository(noteDatabase.noteDao())
    }

    fun provide(context: Context){
        database= Room.databaseBuilder(context,DataDatabase::class.java,"todolist.db").fallbackToDestructiveMigration().build()
        noteDatabase = Room.databaseBuilder(context,NoteDatabase::class.java,"notes.db").build()
    }


}