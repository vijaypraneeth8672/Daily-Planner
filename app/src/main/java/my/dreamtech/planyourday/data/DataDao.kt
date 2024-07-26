package my.dreamtech.planyourday.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addTodo(todoEntity:TodoData)

    @Query("Select * from 'todo_table'")
    abstract  fun getAllTodos() : Flow<List<TodoData>>

    @Update
    abstract suspend fun updateTodo(todoEntity:TodoData)

    @Delete
    abstract suspend fun deleteTodo(todoEntity:TodoData)

    @Query("Select * from 'todo_table' where id =:id")
    abstract fun getTodoById(id:Long) : Flow<TodoData>
}