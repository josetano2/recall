package edu.bluejack23_2.recall.ui.screens.add_deck_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.material3.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.bluejack23_2.recall.ui.components.CustomSnackbarHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeckScreen(viewModel: AddDeckViewModel = viewModel(), snackbarHostState: SnackbarHostState) {

    val scope = rememberCoroutineScope()


    val deckName = viewModel.deckName.value
    val showQuestionDialog = viewModel.showQuestionDialog.value
    val questionText = viewModel.questionText.value
    val answerText = viewModel.answerText.value
    val currentCardIndex = viewModel.currentCardIndex.value
    val cards = viewModel.cards

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp)
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = deckName,
                onValueChange = { viewModel.deckName.value = it },
                label = { Text("Deck Name") }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))


        Row(
            modifier = Modifier.fillMaxWidth()

        ){
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = edu.bluejack23_2.recall.R.drawable.ic_trash),
                contentDescription = "Remove",
                modifier = Modifier.size(30.dp).clickable {
                    if(cards[0].question != "" && cards[0].answer != ""){
                        // Remove
                        viewModel.handleRemoveButtonClick()
                    }
                }
            )
        }

        if (cards.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        viewModel.showQuestionDialog.value = true
                        viewModel.questionText.value = cards[currentCardIndex].question
                        viewModel.answerText.value = cards[currentCardIndex].answer
                    },
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = cards[currentCardIndex].question,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = cards[currentCardIndex].answer,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                viewModel.previousCard()
            }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Previous Card",
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = "Card ${currentCardIndex + 1} of ${cards.size}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(onClick = {
                viewModel.nextCard()
            }) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Next Card",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch {
                    var resp = viewModel.addDeck()
                    snackbarHostState.showSnackbar(resp)
                }
            },
            modifier = Modifier
                .height(50.dp)
                .width(150.dp)
        ) {
            Text("Add Deck", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        if (showQuestionDialog) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.handleCancelButtonClick()
                },
                containerColor = androidx.compose.ui.graphics.Color.White,
                title = {
                    Text(text = "Input Question")
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                    ) {
                        TextField(
                            value = questionText,
                            onValueChange = { viewModel.questionText.value = it },
                            label = { Text("Question") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = answerText,
                            onValueChange = { viewModel.answerText.value = it },
                            label = { Text("Answer") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            scope.launch {
                                var resp = viewModel.handleOkButtonClick()
                                snackbarHostState.showSnackbar(resp)
                            }


                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            viewModel.handleRemoveButtonClick()
                        }
                    ) {
                        Text("Remove")
                    }
                },
            )
        }
    }

}
