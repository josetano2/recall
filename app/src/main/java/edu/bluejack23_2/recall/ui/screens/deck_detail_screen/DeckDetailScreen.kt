package edu.bluejack23_2.recall.ui.screens.deck_detail_screen

import DeckDetailViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.bluejack23_2.recall.R
import edu.bluejack23_2.recall.ui.components.CustomSnackbarHost
import edu.bluejack23_2.recall.ui.components.FlipCard
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailScreen(deckId: String) {
    val viewModel: DeckDetailViewModel = viewModel()
    val cardOptions = listOf(10, 20, 30)
    val modeOptions = listOf("Review", "Rapid")
    var selectedNumberOfCards by remember { mutableStateOf(cardOptions.first()) }
    var selectedMode by remember { mutableStateOf(modeOptions.first()) }
    val navController = LocalNav.current
    val coroutineScope = rememberCoroutineScope();
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(deckId) {
        viewModel.fetchDeckName(deckId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        androidx.compose.material.Scaffold(
            snackbarHost = { CustomSnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize(),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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
                            modifier = Modifier
                                .size(32.dp)
                            //                tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Home",
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp
                        )
                    }
                }
                //            Spacer(modifier = Modifier.height(2.dp))
                //            Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                //            Text(
                //                text = "Deck Details",
                //                modifier = Modifier
                //                    .fillMaxWidth(),
                //                fontSize = 32.sp,
                //                fontWeight = FontWeight.Medium,
                //                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                //            )

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = viewModel.deckName.value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (selectedMode == "Review") {
                    Text(
                        text = "Number of Cards to Review",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        cardOptions.forEach { count ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(80.dp)
                                    .height(60.dp)
                                    .clickable { selectedNumberOfCards = count },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedNumberOfCards == count) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                ),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = count.toString(),
                                        color = if (selectedNumberOfCards == count) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Mode",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    modeOptions.forEach { mode ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(120.dp)
                                .height(60.dp)
                                .clickable { selectedMode = mode },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedMode == mode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = mode,
                                    color = if (selectedMode == mode) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (selectedMode == "Review" && !viewModel.canReview(deckId)) {
                                snackbarHostState.showSnackbar("Deck can only be reviewed once per day!")

                            } else {
                                viewModel.startReview(deckId,selectedMode);
                                navController.navigate(Routes.RECALL_SCREEN + "/${deckId}" + "/${selectedMode}" + "/${selectedNumberOfCards}")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp)
                ) {
                    Text(text = "Start $selectedMode Mode", color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))


            }
        }

    }
}
