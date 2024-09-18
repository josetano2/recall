package edu.bluejack23_2.recall.ui.screens.explore_screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.bluejack23_2.recall.R
import edu.bluejack23_2.recall.ui.components.DeckItem
import edu.bluejack23_2.recall.ui.components.UserItem
import edu.bluejack23_2.recall.viewmodel.ExploreViewModel


@Composable
fun ExploreScreen(viewModel: ExploreViewModel = viewModel()) {
    val decksState by viewModel.decks.collectAsState(initial = emptyList())
    val usersState by viewModel.users.collectAsState(initial = emptyList())

    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current


    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Deck") }
    val options = listOf("Deck", "User")

    LaunchedEffect(Unit) {
        viewModel.getDecks()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .padding(bottom = 108.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                viewModel.performLinearSearch(newValue,selectedOption)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            placeholder = { androidx.compose.material.Text("Search " +selectedOption + "s", color = Color.Black, fontSize = 16.sp) },
            leadingIcon = {
                androidx.compose.material.Icon(
                    painter = painterResource(id = R.drawable.logo_purp_up),
                    contentDescription = "Search Decks",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            },
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(selectedOption)
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        options.forEach { option ->
                            androidx.compose.material.DropdownMenuItem(onClick = {
                                selectedOption = option
                                expanded = false
                            }) {
                                Text(option)
                            }
                        }
                    }
                }
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.performFuzzySearch(searchQuery, selectedOption)
                    keyboardController?.hide()
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        if (selectedOption.equals("Deck")){
            if (decksState.isEmpty()) {
                Text("No decks available")
            } else {
                Log.d("WOI",viewModel.currentUserDecks.value.toString())
                LazyColumn {
                    items(decksState) { deck ->
                        DeckItem(deck = deck, viewModel.currentUserDecks, viewModel.currentUserId, viewModel.loading, viewModel, cannotDelete = false)
                    }
                }
            }
        }
        else if (selectedOption.equals("User")){
            if (usersState.isEmpty()){
                Text("No Users Available")
            }
            else {
                LazyColumn{
                    items(usersState) { user ->
                        UserItem(user = user)
                    }
                }
            }
        }

    }
}





