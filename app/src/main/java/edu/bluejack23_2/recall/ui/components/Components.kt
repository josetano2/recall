package edu.bluejack23_2.recall.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bluejack23_2.recall.R
import edu.bluejack23_2.recall.model.Card
import edu.bluejack23_2.recall.model.Deck
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.model.interface_.DeckViewModel
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.material.Scaffold
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.recall.ui.theme.*
import edu.bluejack23_2.recall.ui.util.SnackbarState

@Composable
fun CustomTextField(
    placeholder: String,
    onTextChanged: (input: String) -> Unit,
    isHidden: Boolean = false,
    value: String = ""
) {

    var currValue by remember {
        mutableStateOf(value);
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        value = currValue,
        onValueChange = {
            currValue = it
            onTextChanged(it)
        },
        placeholder = {
            Text(text = placeholder)
        },
        visualTransformation =
        if (isHidden) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType =
            if (isHidden) {
                KeyboardType.Password
            } else {
                KeyboardType.Text
            }
        )
    )

}

@Composable
fun CustomSpacer() {
    Spacer(Modifier.height(16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckItem(
    deck: Deck,
    deckList: MutableStateFlow<List<String>>,
    currentCreatorId: MutableStateFlow<String>,
    loading: StateFlow<Boolean>,
    viewModel: DeckViewModel,
    cannotDelete: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    val currentDeckList by deckList.collectAsState()
    val isLoading by loading.collectAsState()
    val navController = LocalNav.current

    LaunchedEffect(deck) {
        username = viewModel.getUsername(deck.creatorId)
    }

    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable {
                navController.navigate(Routes.DECK_DETAIL_SCREEN + "/${deck.id}")
            },
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Title
                Text(
                    text = deck.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                    modifier = Modifier.weight(1f)
                )

                // Spacer for separation
                Spacer(modifier = Modifier.width(8.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(36.dp))
                } else {
                    if (!currentDeckList.contains(deck.id)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clickable {
                                    showDialog = true
                                }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_3),
                                contentDescription = "Save Deck",
                                tint = PrimaryDark,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        if (currentCreatorId.value != deck.creatorId && !cannotDelete) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            viewModel.removeDeckFromCurrentUser(deck.id)
                                            deckList.value = deckList.value
                                                .toMutableList()
                                                .apply { remove(deck.id) }
                                        }
                                    }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_trash),
                                    contentDescription = "Remove Deck",
                                    tint = PrimaryDark,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "Created by: $username",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(deck.cards) { card ->
                    CardPreview(card)
                }
            }
        }
    }

    // Dialog for confirming deck addition
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Deck") },
            text = { Text("Are you sure you want to save this deck?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        coroutineScope.launch {
                            viewModel.addDeckToCurrentUser(deck.id)
                            deckList.value = deckList.value.toMutableList().apply { add(deck.id) }
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DeckItem(
//    deck: Deck,
//    deckList: MutableStateFlow<List<String>>,
//    currentCreatorId : MutableStateFlow<String>,
//    loading: StateFlow<Boolean>,
//    viewModel: DeckViewModel,
//    cannotDelete: Boolean
//) {
//    val coroutineScope = rememberCoroutineScope()
//    var username by remember { mutableStateOf("") }
//    val currentDeckList by deckList.collectAsState()
//    val isLoading by loading.collectAsState()
//    var navController = LocalNav.current
//
//    LaunchedEffect(deck) {
//        username = viewModel.getUsername(deck.creatorId)
//    }
//
//    var showDialog by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 16.dp)  .clickable {
//               navController.navigate(Routes.DECK_DETAIL_SCREEN + "/${deck.id}")
//            },
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                // Title
//                Text(
//                    text = deck.name,
//                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
//                    modifier = Modifier.weight(1f)
//                )
//
//                // Spacer for separation
//                Spacer(modifier = Modifier.width(8.dp))
//
//                if (isLoading) {
//                    CircularProgressIndicator(modifier = Modifier.size(36.dp))
//                } else {
//                    if (!currentDeckList.contains(deck.id)) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_add_3),
//                            contentDescription = "Save Deck",
//                            tint = PrimaryDark,
//                            modifier = Modifier
//                                .size(36.dp)
//                                .clickable {
//                                    showDialog = true
//                                },
//                        )
//
//                    } else {
//                        if (currentCreatorId.value == deck.creatorId) {}
//                        else {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_trash),
//                                contentDescription = "Remove Deck",
//                                tint = PrimaryDark,
//                                modifier = Modifier
//                                    .size(36.dp)
//                                    .clickable {
//                                        coroutineScope.launch {
//                                            viewModel.removeDeckFromCurrentUser(deck.id)
//                                            deckList.value = deckList.value
//                                                .toMutableList()
//                                                .apply { remove(deck.id) }
//                                        }
//                                    },
//                            )
//                        }
//
//
//                    }
//                }
//            }
//
//            Text(
//                text = "Created by: $username",
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//
//            LazyRow(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(deck.cards) { card ->
//                    CardPreview(card)
//                }
//            }
//        }
//    }
//
//    // Dialog for confirming deck addition
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("Add Deck") },
//            text = { Text("Are you sure you want to save this deck?") },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        showDialog = false
//                        coroutineScope.launch {
//                            viewModel.addDeckToCurrentUser(deck.id)
//                            deckList.value = deckList.value.toMutableList().apply { add(deck.id) }
//                        }
//                    }
//                ) {
//                    Text("Confirm")
//                }
//            },
//            dismissButton = {
//                Button(
//                    onClick = {
//                        showDialog = false
//                    }
//                ) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPreview(card: Card) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = card.question,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserItem(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {

        Text(user.username)
    }
}

//@Composable
//fun CustomSnackbarHost(snackbarHostState: SnackbarHostState, snackbarViewModel: SnackbarViewModel) {
//    val snackbarState by snackbarViewModel.snackbarState.collectAsState()
//
//    SnackbarHost(hostState = snackbarHostState) {
//        when (val state = snackbarState) {
//            is SnackbarState.Success  -> SnackbarComponent(
//                snackbarHostState,
//                SuccessText,
//                SuccessBackground,
//                painterResource(id = R.drawable.ic_x),
//                state.message
//            )
//            is SnackbarState.Error -> SnackbarComponent(
//                snackbarHostState,
//                ErrorText,
//                ErrorBackground,
//                painterResource(id = R.drawable.ic_x),
//                state.message
//            )
//            else -> SnackbarComponent(
//                snackbarHostState,
//                ErrorText,
//                ErrorBackground,
//                painterResource(id = R.drawable.ic_x)
//            )
//        }
//
//    }
//
//}
//
//@Composable
//fun SnackbarComponent(
//    snackbarHostState: SnackbarHostState,
//    textColor: Color,
//    bgColor: Color,
//    icon: Painter,
//    message: String = "",
//) {
//    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
//        Surface(
//            color = bgColor,
//            contentColor = textColor,
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    painter = icon,
////                    painter = painterResource(id = R.drawable.ic_x),
//                    contentDescription = "Success",
//                    modifier = Modifier
//                        .size(20.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(text = message)
//                Spacer(modifier = Modifier.weight(1f))
//                Text(text = "Close", Modifier.clickable {
//                    snackbarData.dismiss()
//                }, textDecoration = TextDecoration.Underline)
//            }
//        }
//    }
//}

@Composable
fun CustomSnackbarHost(
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
        Surface(
            color = SnackbarBackground,
            contentColor = SnackbarText,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_info),
//                    contentDescription = "Success",
//                    modifier = Modifier
//                        .size(20.dp)
//                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = snackbarData.visuals.message, modifier = Modifier.fillMaxWidth(0.6f))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Close", Modifier.clickable {
                    snackbarData.dismiss()
                }, textDecoration = TextDecoration.Underline)
            }
        }
    }
}
