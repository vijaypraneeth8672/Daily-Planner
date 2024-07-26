package my.dreamtech.planyourday.data

import kotlinx.coroutines.flow.Flow

class DataRepository(private val todoDao: DataDao) {

    suspend fun addTodo(todo:TodoData){
        todoDao.addTodo(todo)
    }

    fun getAllTodos(): Flow<List<TodoData>> = todoDao.getAllTodos()

    fun getTodoById(id:Long) : Flow<TodoData>{
        return todoDao.getTodoById(id)
    }

    suspend fun updateTodo(todo:TodoData){
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo:TodoData){
        todoDao.deleteTodo(todo)
    }
}