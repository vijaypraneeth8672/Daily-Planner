package my.dreamtech.planyourday


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import my.dreamtech.planyourday.data.TodoData
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    id : Long,
    viewModel: MainViewModel,
    navController: NavController
){
    val context = LocalContext.current

    val snackMessage = remember {
        mutableStateOf("")
    }

    val timeState = rememberTimePickerState(11, 30, false)

    val dateState = rememberDatePickerState()
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    val dateToString = millisToLocalDate?.let {
        DateUtils().dateToString(millisToLocalDate)
    } ?: ""

    var showDialog by remember { mutableStateOf(false) }

    var showDateDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if(id!=0L){
        val todo = viewModel.getTodoById(id).collectAsState(initial = TodoData(0L,"","",0,0))
        viewModel.todoDescriptionState = todo.value.description
    }else{
        viewModel.todoDescriptionState =""
    }

    dateState.selectedDateMillis?.let {  }

    Scaffold (
        topBar = {
            TopAppBar {
               Text(text = if(id != 0L) "Edit your task" else "Add a task" )
            }
        },scaffoldState = scaffoldState
    ) {
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    modifier = Modifier.fillMaxWidth(),

                ) {
                    Column(
                        modifier = Modifier
                            .background(color = Color.White.copy(alpha = .3f))
                            .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TimePicker(state = timeState)
                        Row(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(), horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showDialog = false }) {
                                Text(text = "Dismiss")
                            }
                            TextButton(onClick = {
                                showDialog = false
                            }) {
                                Text(text = "Confirm")
                            }
                        }
                    }
                }
            }

        if (showDateDialog) {
            AlertDialog(
                onDismissRequest = { showDateDialog = false },
                modifier = Modifier.fillMaxWidth(),

                ) {
                Column(
                    modifier = Modifier
                        .background(color = Color.White.copy(alpha = .3f))
                        .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DatePicker(state = dateState)

                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showDateDialog = false }) {
                            Text(text = "Dismiss")
                        }
                        TextButton(onClick = {
                            showDateDialog = false
                        }) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }

        if(!showDialog && !showDateDialog){
            Column (
                modifier = Modifier
                    .padding(it)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                TodoTextField(label = "Task", value = viewModel.todoDescriptionState, modifier = Modifier.fillMaxWidth(),
                    onValueChange = {newString->
                        viewModel.onTodoDescChanged(newString)
                    })

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    TodoTextField(label ="Set Time", value = "${timeState.hour} : ${timeState.minute}" , modifier = Modifier.weight(3f), onValueChange ={})
                    IconButton(modifier = Modifier.weight(1f), onClick = {
                        showDialog = true
                    }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_access_time_24), contentDescription = "Time Picker" )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    TodoTextField(label ="Set Date", value = dateToString, modifier = Modifier.weight(3f), onValueChange ={})
                    IconButton(modifier = Modifier.weight(1f), onClick = {
                        showDateDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date Picker" )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {
                    if(viewModel.todoDescriptionState.isNotEmpty()){
                        if(id!=0L){
                            viewModel.updateTodo(
                                TodoData(id = id,
                                    description = viewModel.todoDescriptionState.trim(),
                                    date = dateToString,
                                    hour = timeState.hour,
                                    minute = timeState.minute)
                            )
                        }else{
                            viewModel.addTodo(
                                TodoData(description = viewModel.todoDescriptionState.trim(),
                                    date = dateToString,
                                    hour = timeState.hour,
                                    minute = timeState.minute)
                            )
                            snackMessage.value = "Task has been added"
                        }
                    }else{
                        snackMessage.value ="Enter fields to add a task"
                    }
                    scope.launch {
                        //scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
                        navController.navigateUp()
                    }
                }) {
                    Text(text = if(id!=0L) "Update Task" else "Add Task",
                        style = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                }



            }
        }
    }
}



@Composable
fun TodoTextField(
    label: String,
    value:String,
    modifier: Modifier,
    onValueChange: (String) -> Unit
){
  OutlinedTextField(value = value, onValueChange = onValueChange,
      label ={ Text(text = label, color = Color.Black)},
      modifier = modifier,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
      colors = TextFieldDefaults.outlinedTextFieldColors(
          textColor = Color.Black,
          focusedBorderColor = Color.Cyan,
          unfocusedBorderColor = Color.Black,
          cursorColor = Color.Black,
          focusedLabelColor = Color.Cyan,
          unfocusedLabelColor = Color.Black
      )
  )
}

