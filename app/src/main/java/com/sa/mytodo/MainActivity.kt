package com.sa.mytodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sa.mytodo.ui.navigation.Screen
import com.sa.mytodo.presentation.add_todo.AddToDoScreen
import com.sa.mytodo.presentation.todo_list.ToDoListingScreen
import com.sa.mytodo.ui.theme.MyTodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MyTodoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {innerPadding ->
                        NavHost(navController = navController,
                            startDestination = Screen.ToDoList.route,
                            modifier = Modifier.padding(innerPadding)) {
                            composable(Screen.ToDoList.route) {
                                ToDoListingScreen(
                                    navController = navController,
                                )
                            }
                            composable(
                                "${Screen.AddToDo.route}/{uid}",
                                arguments = listOf(navArgument("uid") { type = NavType.IntType })
                            ) {
                                AddToDoScreen(
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
