package my.dreamtech.planyourday.notedata

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [NoteData::class],
    version =1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun noteDao(): NoteDao
}