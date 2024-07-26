package my.dreamtech.planyourday

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.dreamtech.planyourday.data.DataRepository
import my.dreamtech.planyourday.data.Graph
import my.dreamtech.planyourday.data.TodoData
import java.util.Calendar

class MainViewModel(
    private val dataRepository : DataRepository = Graph.dataRepository
) : ViewModel(){

    private val _currentScreen : MutableState<Screen> = mutableStateOf(Screen.DrawerScreen.ToDo)

    val currentScreen: MutableState<Screen> get() = _currentScreen



    fun setCurrentScreen(screen: Screen){
        _currentScreen.value = screen
    }

    var todoDescriptionState by mutableStateOf("")

    var selectedDate by  mutableStateOf(Calendar.getInstance())
    var selectedTime by mutableStateOf(Calendar.getInstance())


    fun onTodoDescChanged(newString : String){
        todoDescriptionState = newString
    }

    lateinit var getAllTodos: Flow<List<TodoData>>

    init {
        viewModelScope.launch {
            getAllTodos = dataRepository.getAllTodos()
        }
    }

    fun addTodo(todoData: TodoData){
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.addTodo(todoData)
        }
    }

    fun getTodoById(id: Long) : Flow<TodoData>{
        return dataRepository.getTodoById(id)
    }

    fun updateTodo(todoData: TodoData){
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.updateTodo(todoData)
        }
    }

    fun deleteTodo(todoData: TodoData){
        viewModelScope.launch (Dispatchers.IO){
            dataRepository.deleteTodo(todoData)
            getAllTodos = dataRepository.getAllTodos()
        }
    }

}