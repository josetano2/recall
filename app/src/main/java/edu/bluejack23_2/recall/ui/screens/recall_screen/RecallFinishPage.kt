package edu.bluejack23_2.recall.ui.screens.recall_screen

import ScoredCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.bluejack23_2.recall.ui.theme.PrimaryDark
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes
import kotlinx.coroutines.coroutineScope
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecallFinishPage(storedCards: List<ScoredCard>?, mode : String) {
    val viewModel: RecallFinishViewModel = viewModel()

    storedCards?.let {
        viewModel.setStoredCards(it)
        viewModel.setMode(mode);
    }

    LaunchedEffect(viewModel.storedCards) {
        viewModel.supermemoSave()
    }

    val colors = mapOf(
        "again" to Color(0xFFC64633),
        "hard" to Color(0xFFDA8721),
        "good" to Color(0xFF138EB0),
        "easy" to Color(0xFF61A132)
    )

    val responseCounts = storedCards?.groupingBy { it.response.lowercase(Locale.ROOT) }?.eachCount() ?: emptyMap()
    val maxCount = responseCounts.values.maxOrNull() ?: 0
    val navController = LocalNav.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black,
                    fontSize = 20.sp
                )

                Button(
                    onClick = { navController.navigate(Routes.HOME_SCREEN) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = "Back",
                        style = TextStyle(
                            color = PrimaryDark,
                            fontSize = 18.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Bar plot section
                responseCounts.forEach { (response, count) ->
                    val barColor = colors[response] ?: Color.Gray
                    val barWidth = if (maxCount > 0) (count.toFloat() / maxCount) * 300 else 0f
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = response.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            style = MaterialTheme.typography.bodyMedium,
                            color = barColor,
                            modifier = Modifier.width(60.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .width(barWidth.dp)
                                .background(barColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = count.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(modifier = Modifier.fillMaxWidth())

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                // Response list section
                storedCards?.let {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(storedCards) { scoredCard ->
                            val responseColor = colors[scoredCard.response.toLowerCase()] ?: Color.Gray
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Question: ${scoredCard.card.question}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Answer: ${scoredCard.card.answer}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Your Response: ${scoredCard.response}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = responseColor
                                    )
                                }
                            }
                        }
                    }
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No stored cards available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
