package edu.bluejack23_2.recall.ui.screens.homepage_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.bluejack23_2.recall.R
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.bluejack23_2.recall.ui.components.DeckItem

@Composable
fun HomepageActualScreen (viewModel: HomepageViewModel = viewModel()){
    var searchQuery by remember { mutableStateOf("") }
    val decksState by viewModel.decks.collectAsState(initial = emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.getDecks()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top=8.dp)
            .padding(bottom = 108.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        androidx.compose.material3.TextField(
            value = searchQuery,
            onValueChange = { newValue ->
                run {
                    searchQuery = newValue
                    viewModel.performLinearSearch(newValue,null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Search Your Decks", color = Color.Black) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.logo_purp_up),
                    contentDescription = "Search Decks",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.performFuzzySearch(searchQuery,null)
                    keyboardController?.hide()
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )


        if (decksState.isEmpty()) {
            androidx.compose.material3.Text("No decks available")
        } else {
            LazyColumn {
                items(decksState) { deck ->
                    DeckItem(deck = deck,viewModel.currentUserDecks,viewModel.currentUserId, viewModel.loading, viewModel, cannotDelete = true)
                }
            }
        }
    }
}