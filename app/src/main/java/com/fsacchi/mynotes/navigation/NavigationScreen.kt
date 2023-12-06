package com.fsacchi.mynotes.navigation

sealed class Screen(
    val route: String,
    val objectName: String = "",
    val objectPath: String = ""
) {
    object Home :
        Screen("home_screen")

    object Note :
        Screen("note_screen", objectName = "noteItem", objectPath = "/{noteItem}")
}