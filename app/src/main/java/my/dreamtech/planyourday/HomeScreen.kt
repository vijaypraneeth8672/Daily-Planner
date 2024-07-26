package my.dreamtech.planyourday

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){

    val scope : CoroutineScope = rememberCoroutineScope()
    val scaffoldState : ScaffoldState = rememberScaffoldState()

    val viewModel : MainViewModel = viewModel()

    val noteViewModel: NoteViewModel = viewModel()

    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    var topAppBarClicked by remember {
        mutableStateOf(true)
    }
    var SearchBarClicked by remember {
        mutableStateOf(false)
    }




    Scaffold (
        topBar = {
            if(topAppBarClicked){
                TopAppBar(title = { Text(text = title.value) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(painter = painterResource(id = R.drawable.baseline_dehaze_24),
                                contentDescription = "Menu")
                        }

                    }
                    )
            }
        },scaffoldState = scaffoldState,
        drawerContent = {
            LazyColumn(Modifier.padding(16.dp)){
                items(screensInDrawer){
                    item->
                    DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                              scope.launch {
                                  scaffoldState.drawerState.close()
                              }
                              controller.navigate(item.dRoute)
                              title.value = item.dTitle
                    }

                }

            }
        }, floatingActionButton = {
            if(currentRoute == Screen.DrawerScreen.ToDo.dRoute || currentRoute == Screen.DrawerScreen.Notes.dRoute){

                FloatingActionButton(modifier = Modifier.padding(20.dp),
                    contentColor = Color.White,
                    backgroundColor = Color.Black,onClick = {
                        if(currentRoute == Screen.DrawerScreen.ToDo.dRoute ) {
                            controller.navigate(Screen.AddScreen.route + "/0L")
                        }else if(currentRoute == Screen.DrawerScreen.Notes.dRoute){
                            controller.navigate(Screen.NoteAddScreen.route + "/0L")
                        }
                    }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add" )
                }
            }
        }, backgroundColor = colorResource(id = R.color.ocean_blue)
    ) {pd ->

        
       my.dreamtech.planyourday.Navigation(navController = controller, viewModel = viewModel , noteViewModel = noteViewModel, pd = pd)

    }

}

@Composable
fun DrawerItem(
    selected : Boolean,
    item : Screen.DrawerScreen,
    onDrawerItemClicked :() -> Unit
){
    val background = if(selected) colorResource(id = R.color.ocean_blue) else Color.White

    Row (
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background, shape = RoundedCornerShape(16.dp),)
            .clickable {
                onDrawerItemClicked()
            }
    ){
        Icon(painter = painterResource(id = item.icon), contentDescription = item.dTitle,
            Modifier.padding(end = 8.dp,top =4.dp))

        Text(text = item.dTitle,
            style = MaterialTheme.typography.h5)

    }


}

@Composable
fun Navigation(navController: NavController,viewModel: MainViewModel,noteViewModel: NoteViewModel,pd:PaddingValues){
    NavHost(navController =navController as NavHostController, startDestination = Screen.DrawerScreen.ToDo.route,
        modifier = Modifier.padding(pd)){

        composable(Screen.DrawerScreen.ToDo.dRoute){
            ToDo(navController,viewModel)
        }
        composable(Screen.DrawerScreen.Notes.dRoute){
            Notes(navController,noteViewModel)
        }
        composable(Screen.AddScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )
        ){entry->
            val id = if(entry.arguments != null) entry.arguments!!.getLong("id") else 0L
            AddEditScreen(id = id, viewModel = viewModel , navController = navController )

        }
        composable(Screen.NoteAddScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )
        ){entry->
            val id = if(entry.arguments != null) entry.arguments!!.getLong("id") else 0L
            NoteAddEditScreen(id = id, viewModel = noteViewModel , navController = navController )

        }


        
    }
}
