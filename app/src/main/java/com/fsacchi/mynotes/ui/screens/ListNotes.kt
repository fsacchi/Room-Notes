package com.fsacchi.mynotes.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fsacchi.mynotes.R
import com.fsacchi.mynotes.data.local.entity.Note
import com.fsacchi.mynotes.navigation.Screen
import com.fsacchi.mynotes.presentation.features.note.NoteViewModel

@Composable
fun ListNotes(
    navController: NavController,

) {
    val noteViewModel = hiltViewModel<NoteViewModel>()

    LaunchedEffect(key1 = 0) {
        noteViewModel.getAllNotes()
    }

    if (noteViewModel.allNotes.value != null) {
        HeaderNotes(navController, noteViewModel.allNotes.value, noteViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeaderNotes(navController: NavController, listNotes: List<Note>?, viewModel: NoteViewModel) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var noteSelected by remember { mutableStateOf<Note?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.title_list_note),
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(id = R.string.title_quantity_note, listNotes?.size ?: 0),
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.padding(16.dp))

        listNotes?.let { listNotes ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(items = listNotes, itemContent = {
                    Card(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            noteSelected = it
                            showBottomSheet = true
                        }) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = it.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = TextUnit(16f, TextUnitType.Sp)
                            )
                            Text(text = it.note,
                                modifier = Modifier.padding(top = 8.dp),
                                fontSize = TextUnit(14f, TextUnitType.Sp)
                            )
                        }
                    }
                })
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clickable {
                            noteSelected?.let {
                                navController.navigate(Screen.Note.route.plus("/${it.id}"))
                            }
                            showBottomSheet = false
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(id = R.string.edit))
                        Text (text = stringResource(id = R.string.edit),
                            modifier = Modifier.padding(start = 16.dp),
                            fontWeight = FontWeight.SemiBold)
                    }

                    Divider(Modifier.padding(4.dp), thickness = 1.dp, color = Color.LightGray)

                    Row(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clickable {
                            noteSelected?.let {
                                viewModel.delete(it)
                            }
                            showBottomSheet = false
                        }) {
                        Icon(imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete),
                            tint = Color.Red)
                        Text (text = stringResource(id = R.string.delete),
                            modifier = Modifier.padding(start = 16.dp),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Red)
                    }

                    Spacer(modifier = Modifier.padding(bottom = 16.dp))
                }
            }
        }

    }
}