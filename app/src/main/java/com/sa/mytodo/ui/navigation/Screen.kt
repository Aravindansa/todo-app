package com.sa.mytodo.ui.navigation

sealed class Screen(val route:String){
    data object ToDoList: Screen("to_do_list")
    data object AddToDo: Screen("add_to_do}")
    data object ToDoDetail: Screen("to_do_detail")
}