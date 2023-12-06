package com.fsacchi.mynotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fsacchi.mynotes.R
import com.fsacchi.mynotes.data.local.entity.Note
import com.fsacchi.mynotes.presentation.features.note.NoteViewModel
import com.fsacchi.mynotes.ui.component.AlertDialog
import kotlinx.coroutines.launch

@Composable
fun Notes(
    navController: NavController, noteId: Int,
    onSaveButton: MutableState<Boolean>
) {
    val noteViewModel = hiltViewModel<NoteViewModel>()

    LaunchedEffect(key1 = 0) {
        if (noteId > 0) {
            // edit mode, get note in database
            noteViewModel.getNoteById(noteId)
        }
    }

    if (noteViewModel.note.value != null) {
        NoteScreen(navController, onSaveButton, noteViewModel, noteViewModel.note.value)
    } else {
        NoteScreen(navController, onSaveButton, noteViewModel)
    }

}

@Composable
fun NoteScreen(navController: NavController, onSaveButton: MutableState<Boolean>,
               noteViewModel: NoteViewModel, noteSelected: Note? = null) {

    var titleText by remember { mutableStateOf(noteSelected?.title ?: "") }
    var contentText by remember { mutableStateOf(noteSelected?.note ?: "") }
    var isErrorTitle by rememberSaveable { mutableStateOf(false) }
    var isErrorNote by rememberSaveable { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(onSaveButton.value) {
        coroutineScope.launch {
            if (onSaveButton.value) {
                onSaveButton.value = false
                isErrorNote = contentText.isEmpty()
                isErrorTitle = titleText.isEmpty()

                if (!isErrorNote && !isErrorTitle) {
                    if (noteSelected != null) {
                        with(noteSelected) {
                            title = titleText
                            note = contentText
                        }.also {
                            noteViewModel.update(noteSelected)
                        }
                    } else {
                        noteViewModel.insert(titleText, contentText)
                    }
                } else {
                    showError = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = stringResource(id = R.string.note),
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = titleText,
            onValueChange = {
                titleText = it
                isErrorTitle = titleText.isEmpty()
            },
            label = { Text(stringResource(id = R.string.title)) },
            isError = isErrorTitle,
            maxLines = 1,
            textPlaceHolder = stringResource(id = R.string.placeholder_title),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = contentText,
            onValueChange = {
                contentText = it
                isErrorNote = contentText.isEmpty()
            },
            label = { Text(stringResource(id = R.string.note)) },
            isError = isErrorNote,
            maxLines = 15,
            textPlaceHolder = stringResource(id = R.string.placeholder_note),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    if(showError) {
        AlertDialog(message = stringResource(id = R.string.error_required_fields)) {
            showError = false
        }
    }

    noteViewModel.insertState.let { insertState ->
        if (insertState.value != null) {
            insertState.value = null
            navController.popBackStack()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    label: @Composable () -> Unit,
    maxLines: Int = 1,
    textPlaceHolder: String,
    modifier: Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = { Text(textPlaceHolder) },
        maxLines = maxLines,
        isError = isError,
        colors = TextFieldDefaults.textFieldColors(containerColor = Color.LightGray),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        modifier = modifier
    )
}


