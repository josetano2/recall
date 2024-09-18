package edu.bluejack23_2.recall.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack23_2.recall.R
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.ui.components.CustomSpacer
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes
import kotlinx.coroutines.tasks.await

@Composable
fun LandingScreen() {
    var navController = LocalNav.current

    LaunchedEffect(Unit){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
           navController.navigate(Routes.HOME_SCREEN)
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_purp_up),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp),
                contentDescription = "Logo",

                )
//            CustomSpacer()
            Text(
                text = "Review, Respond, Recall",
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(weight = 800),
                lineHeight = 34.sp
            )
            CustomSpacer()
            Button(
                onClick = {
                    navController.navigate(Routes.REGISTER_SCREEN)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Create a new account",
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(4.dp))
            Button(
                onClick = {
                    navController.navigate(Routes.LOGIN_SCREEN)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Log In",
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(4.dp))
//
//            Button(
//                onClick = {
//                    navController.navigate(Routes.HOME_SCREEN)
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(54.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.background,
//                    contentColor = MaterialTheme.colorScheme.primary
//                )
//            ) {
//                Text(
//                    text = "Homepage",
//                    fontSize = 16.sp
//                )
//            }

        }

    }
}

@Preview
@Composable
fun LandingScreenPreview() {
    LandingScreen()
}