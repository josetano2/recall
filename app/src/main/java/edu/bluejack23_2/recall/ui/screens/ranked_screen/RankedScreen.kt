package edu.bluejack23_2.recall.ui.screens.ranked_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankedScreen() {
    val rankedItems = listOf(
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 4",
        "Item 5"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Ranked Page",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Feature Not Available Yet")

//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(rankedItems) { item ->
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        elevation = CardDefaults.cardElevation(4.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            Text(
//                                text = item,
//                                style = MaterialTheme.typography.bodyLarge,
//                                color = Color.Black
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Additional information about $item",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = Color.Gray
//                            )
//                        }
//                    }
//                }
//            }
        }
    }
}
