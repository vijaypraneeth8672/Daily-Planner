package my.dreamtech.planyourday

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import my.dreamtech.planyourday.notedata.NoteData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes(
    navController: NavController,
    viewModel: NoteViewModel
){
    val context = LocalContext.current
    val notes = viewModel.getAllNotes.collectAsState(initial = listOf())

    var text by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }

    val filteredNotes = remember(notes.value,text) {
        notes.value.filter { note->
            note.title.contains(text,ignoreCase = true)
        }
    }

    Column(modifier= Modifier
        .fillMaxSize()
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
                Text(text = "Search Notes")
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
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(filteredNotes,key ={ note ->
                note.id
            }){
                note ->
                NoteItem(noteData = note, viewModel = viewModel , context = context){
                    val id = note.id
                    navController.navigate(Screen.NoteAddScreen.route + "/$id")
                }
            }
        }
    }
}

@Composable
fun NoteItem(noteData: NoteData,viewModel: NoteViewModel,
             context: Context,onClick:() -> Unit){
    val scope : CoroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .clickable { onClick() },
        elevation = 10.dp,
        backgroundColor = Color.White
    ) {
       Row (
           modifier = Modifier
               .fillMaxSize()
               .padding(8.dp),
           verticalAlignment = Alignment.CenterVertically
           , horizontalArrangement = Arrangement.SpaceBetween
       ){
               Column(Modifier.weight(1f)) {
                   Text(text = noteData.title, fontWeight = FontWeight.SemiBold,
                       color = Color.Black)

                   Spacer(modifier = Modifier.height(8.dp))
                   Text(text = noteData.description,
                       color = Color.Black)
               }


           IconButton(onClick ={
               scope.launch {
                   try {
                       viewModel.deleteNote(noteData)
                   }catch (e: Exception) {
                       e.printStackTrace()
                       Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                   }
               }
           }) {
               Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete button")
           }
       }
    }
}