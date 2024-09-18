package edu.bluejack23_2.recall.ui.screens.recall_screen

import Border
import Borders
import RecallViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import border
import edu.bluejack23_2.recall.R
import edu.bluejack23_2.recall.ui.components.FlipCard
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes

@Composable
fun RecallScreen(deckId: String, mode: String, numberOfCards : Int) {
    val viewModel: RecallViewModel = viewModel()
    val deck by viewModel.deck
    val cards by viewModel.cards
    var flipped: Boolean by remember { mutableStateOf(false) }
    var navController: NavController = LocalNav.current

    LaunchedEffect(deckId) {
        viewModel.fetchDeck(deckId,mode, numberOfCards)
    }

    viewModel.setOnLastCardReachedCallback {
        navController.currentBackStackEntry?.savedStateHandle?.set("storedCards", viewModel.storedCards.value)
        navController.navigate(Routes.RECALL_FINISH_SCREEN  + "/${mode}")
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.popBackStack()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_left_arrow),
                        contentDescription = "",
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Home",
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Question and Answer Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (deck != null && cards.isNotEmpty()) {


                    FlipCard(
                        frontContent = {
                            Text(
                                text = cards[viewModel.index.value].question,
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .fillMaxWidth()
                            )
                        },
                        backContent = {
                            Text(
                                text = cards[viewModel.index.value].question,
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .fillMaxWidth()
                            )

                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .padding(vertical = 8.dp)
                            )

                            if (flipped) {
                                Text(
                                    text = cards[viewModel.index.value].answer,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .fillMaxWidth()
                                )
                            }

                        },
                        rotated = flipped,
                        onCardClick = { }
                    )
                } else {

                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Divider
            // Button Row Section

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val colors = listOf(
                    Color(0xFFC64633), //  again
                    Color(0xFFDA8721), // hard
                    Color(0xFF138EB0), // good
                    Color(0xFF61A132)  // easy
                )

                var flipColor = Color(0xFFC07EC5);

                val texts = listOf("AGAIN", "HARD", "GOOD", "EASY")
                val textColors = listOf(Color.Black, Color.Red, Color.Green, Color.Blue)

                if (!flipped) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Transparent)
                            .border(top = Border(2.dp, flipColor))
                            .clickable(onClick = {
                                flipped = !flipped
                            }),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Flip",
                            color = flipColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 16.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    for (i in texts.indices) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.Transparent)
                                .border(top = Border(2.dp, colors[i]))
                                .clickable(onClick = {
                                    flipped = !flipped
                                    viewModel.nextCard(texts[i])
                                }),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = texts[i],
                                color = colors[i],
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 16.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

            }
        }
    }
}
