package my.dreamtech.planyourday

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import my.dreamtech.planyourday.data.TodoData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDo(
    navController: NavController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val todoList = viewModel.getAllTodos.collectAsState(initial = listOf())

    var text by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }

    val filteredTodoList = remember(todoList.value, text) {
        todoList.value.filter { todo ->
            todo.description.contains(text, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            query = text,
            onQueryChange = {
                text = it
            },
            onSearch = {
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(text = "Search Tasks")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon"
                    )
                }
            }
        ){

        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(filteredTodoList, key = { todo -> todo.id }) { todo ->
                TodoItem(todoData = todo, viewModel, context) {
                    val id = todo.id
                    navController.navigate(Screen.AddScreen.route + "/$id")
                }
                Log.d("ToDo", "TodoList size: ${todoList.value.size}")
            }
        }
    }
}


@Composable
fun TodoItem(todoData: TodoData, viewModel: MainViewModel, context: Context, onClick: () -> Unit) {
    var checkedState by remember { mutableStateOf(false) }
    val scope: CoroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .clickable { onClick() },
        elevation = 10.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, start = 8.dp, bottom = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(todoData.date.isNotEmpty() && todoData.hour !=0 && todoData.minute != 0){
                Column (
                    Modifier.weight(2f)
                ) {
                    Text(text = todoData.description)
                    Text(text = "${todoData.hour}:${todoData.minute} on ${todoData.date}")
                }
            }else{
                Text(text = todoData.description)
            }

            Checkbox(
                checked = checkedState,
                onCheckedChange = {
                    checkedState = it
                    if (it) {
                        // Use the coroutine scope to launch the deletion in a background thread
                        scope.launch {
                            try {
                                viewModel.deleteTodo(todoData = todoData)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            )
        }
    }
}
