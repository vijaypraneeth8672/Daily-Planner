package my.dreamtech.planyourday

import androidx.annotation.DrawableRes

sealed class Screen( val route: String, val title : String){

    object AddScreen : Screen("add_screen","Add Your Task")
    object NoteAddScreen : Screen("note_add_screen","Add a Note")

    sealed class DrawerScreen(val dRoute: String, val dTitle : String, @DrawableRes val icon : Int)
        : Screen(dRoute,dTitle) {
            object ToDo : DrawerScreen(
                "todo",
                "To Do",
                R.drawable.baseline_add_task_todo__24
            )

            object Notes : DrawerScreen(
                "notes",
                "Notes",
                R.drawable.baseline_note_add_24
            )
        }
}

val screensInDrawer = listOf(
    Screen.DrawerScreen.Notes,
    Screen.DrawerScreen.ToDo
)
