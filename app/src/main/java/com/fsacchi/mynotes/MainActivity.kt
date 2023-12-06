package com.fsacchi.mynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.fsacchi.mynotes.navigation.Navigation
import com.fsacchi.mynotes.navigation.Screen
import com.fsacchi.mynotes.navigation.currentRoute
import com.fsacchi.mynotes.ui.theme.MyNotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldNotes()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class)
@Composable
fun ScaffoldNotes() {
    val navController = rememberNavController()
    val keyboardController = LocalSoftwareKeyboardController.current
    val fabActionState = remember { mutableStateOf(false) }

    val onBackClick: () -> Unit = {
        keyboardController?.hide()
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            when(currentRoute(navController)) {
                Screen.Note.route -> {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(imageVector = Icons.Default.ArrowBack
                                    ,contentDescription = stringResource(id = R.string.back))
                            }
                        }
                    )
                }
                else -> {}
            }
        },
        floatingActionButton = {
            when (currentRoute(navController)) {
                Screen.Home.route -> {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Screen.Note.route.plus("/${0}"))
                        },
                        modifier = Modifier
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.add))
                    }
                }
                else -> {
                    FloatingActionButton(
                        onClick = {
                            fabActionState.value = true
                        },
                        modifier = Modifier
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = stringResource(
                            id = R.string.done
                        ))
                    }
                }
            }

        }
    ) {
        Navigation(navController, Modifier.padding(it), fabActionState)
    }
}
