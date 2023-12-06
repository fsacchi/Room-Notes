package com.fsacchi.mynotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.fsacchi.mynotes.ui.screens.ListNotes
import com.fsacchi.mynotes.ui.screens.Notes

@Composable
fun Navigation(
    navController: NavHostController, modifier: Modifier, onFloatingButton: MutableState<Boolean>
) {
    NavHost(navController, startDestination = Screen.Home.route, modifier) {
        composable(Screen.Home.route) {
            ListNotes(navController = navController)
        }
        composable(
            Screen.Note.route.plus(Screen.Note.objectPath),
            arguments = listOf(navArgument(Screen.Note.objectName) {
                type = NavType.IntType
            })
        ) { backStack ->
            val noteId = backStack.arguments?.getInt(Screen.Note.objectName) ?: 0
            Notes(navController = navController, noteId, onFloatingButton)
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}

