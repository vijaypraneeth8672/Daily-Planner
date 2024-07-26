package my.dreamtech.planyourday.data

import androidx.room.Database
import androidx.room.RoomDatabase




@Database(
    entities = [TodoData::class],
    version = 3,
    exportSchema = false
)
abstract class DataDatabase : RoomDatabase() {

    abstract fun todoDao() : DataDao
}