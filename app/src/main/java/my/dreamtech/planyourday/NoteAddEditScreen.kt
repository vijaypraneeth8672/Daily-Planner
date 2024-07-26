package my.dreamtech.planyourday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import my.dreamtech.planyourday.data.TodoData
import my.dreamtech.planyourday.notedata.NoteData


@Composable
fun NoteAddEditScreen(
    id: Long,
    viewModel: NoteViewModel,
    navController: NavController
){
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if(id!=0L){
        val note = viewModel.getNoteById(id).collectAsState(initial = NoteData(0L,"",""))
        viewModel.noteTitleState = note.value.title
        viewModel.noteDescriptionState = note.value.description
    }else{
        viewModel.noteTitleState =""
        viewModel.noteDescriptionState =""
    }

    Scaffold (
        topBar = {
            TopAppBar(title = {Text(text = if(id != 0L) "Edit Note" else "Add a Note" )},
                navigationIcon = {
                IconButton(onClick ={
                    if(viewModel.noteTitleState.isNotEmpty() && viewModel.noteDescriptionState.isNotEmpty()){
                        if(id!=0L){
                            viewModel.updateNote(
                                NoteData(id = id,
                                    title = viewModel.noteTitleState.trim(),
                                    description = viewModel.noteDescriptionState.trim())
                            )
                        }else{
                            viewModel.addNote(
                                NoteData(title= viewModel.noteTitleState.trim(),
                                    description = viewModel.noteDescriptionState.trim())
                            )

                        }
                    }else{
                        //
                    }
                    scope.launch {

                        navController.navigateUp()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Add" )
                }

            })
        },scaffoldState = scaffoldState
    ) {
        Column (
            modifier = Modifier
                .padding(it)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            NoteTextField(label = "Title", value = viewModel.noteTitleState,
               modifier = Modifier.fillMaxWidth(),
                onValueChange = {newString->
                    viewModel.onNoteTitleChanged(newString)
                })

            Spacer(modifier = Modifier.height(10.dp))

            Box(Modifier.fillMaxWidth()) {
                NoteTextField(label = "Description", value = viewModel.noteDescriptionState,
                    modifier = Modifier
                        .fillMaxSize(),
                    onValueChange = {newString->
                        viewModel.onNoteDescChanged(newString)
                    })
            }

        }
    }


}

@Composable
fun NoteTextField(
    label: String,
    value:String,
    modifier: Modifier,
    onValueChange: (String) -> Unit
){
    OutlinedTextField(value = value, onValueChange = onValueChange,
        label ={ Text(text = label, color = Color.Black) },
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