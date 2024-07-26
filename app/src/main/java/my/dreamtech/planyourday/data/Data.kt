package my.dreamtech.planyourday.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_table")
 data class TodoData (
    @PrimaryKey(autoGenerate = true)
     val id : Long = 0L,
     @ColumnInfo(name = "todo_desc")
     val description : String ="",
     val date : String ="",// Time in milliseconds
     val hour: Int, // Hour of the reminder
     val minute: Int // Minute of the reminder
)

