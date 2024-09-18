package edu.bluejack23_2.recall.ui.screens.profile_screen

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
//import androidx.biometric.BiometricManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import edu.bluejack23_2.recall.R
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.repository.ImageRepository
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.theme.BackgroundLightGradient
import edu.bluejack23_2.recall.ui.theme.RainbowBorder
import edu.bluejack23_2.recall.ui.util.BiometricPromptManager
import edu.bluejack23_2.recall.ui.util.BiometricPromptManager.*
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import edu.bluejack23_2.recall.ui.theme.LightRed
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, promptManager: BiometricPromptManager) {

    val biometricResult by promptManager.promptResults.collectAsState(initial = null)
    val navController = LocalNav.current
    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            println("Tes")
        }
    )

    LaunchedEffect(biometricResult) {
        if (biometricResult is BiometricResult.AuthenticationNotSet) {
            if (Build.VERSION.SDK_INT >= 30) {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                enrollLauncher.launch(enrollIntent)
            }
        }

    }

    LaunchedEffect(Unit) {
        promptManager.showBiometricPrompt(
            title = "Authenticate User",
            description = "User Verification"
        )
    }

    biometricResult?.let { result ->
        when (result) {
            is BiometricResult.AuthenticationError -> navController.navigate(Routes.HOME_SCREEN)
            BiometricResult.AuthenticationFailed -> navController.navigate(Routes.HOME_SCREEN)
            BiometricResult.AuthenticationNotSet -> navController.navigate(Routes.HOME_SCREEN)
            BiometricResult.AuthenticationSuccess -> ProfileContent(profileViewModel = profileViewModel)
            BiometricResult.FeatureUnavailable -> navController.navigate(Routes.HOME_SCREEN)
            BiometricResult.HardwareUnavailable -> navController.navigate(Routes.HOME_SCREEN)
        }

    }

//    ProfileContent(profileViewModel)


}

@Composable
fun ProfileContent(profileViewModel: ProfileViewModel) {

    val navController = LocalNav.current
    val user by profileViewModel.user.collectAsState(initial = null)
    val context = LocalContext.current
    val imageRepository = ImageRepository()
    val userRepository = UserRepository()


    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val scope = rememberCoroutineScope()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            if (selectedImageUri != null) {
                imageRepository.uploadImage(selectedImageUri!!, context) { downloadUri ->
                    // update pfp attribute to storage
//                    Log.d("asdadsa", "$downloadUri")
                    userRepository.updateProfilePicture(user!!, downloadUri.toString())
                    profileViewModel.getUserData()
                }
            }

        }
    )

    LaunchedEffect(Unit) {
        profileViewModel.getUserData()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = BackgroundLightGradient),
//        color = MaterialTheme.colorScheme.background
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            if (user != null) {
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ImageBorderAnimation(user!!.pfp, singlePhotoPickerLauncher)
                        Spacer(modifier = Modifier.width(32.dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${user!!.username}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_right_arrow),
                                    contentDescription = "Profile Detail",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            navController.navigate(Routes.PROFILE_UPDATE_DETAIL_SCREEN)
                                        },
                                    tint = MaterialTheme.colorScheme.primary
                                )

                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${user!!.email}",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
//                    Spacer(modifier = Modifier.height(24.dp))
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp),
//                        shape = RoundedCornerShape(20.dp),
//                        color = Color.White,
//                        shadowElevation = 4.dp
//
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxSize(),
//                        ) {
//
//                        }
//                    }
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = {
//                            navController.navigate(Routes.REGISTER_SCREEN)
                            scope.launch {
                                profileViewModel.logout()
                                navController.navigate(Routes.LANDING_SCREEN)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = LightRed
                        ),
                        border = BorderStroke(1.dp, LightRed)
                    ) {
                        Text(
                            text = "Log out",
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(72.dp))
                }
            } else {
                Text(
                    text = "No User",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
fun ImageBorderAnimation(
    pfp: String,
    singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) {

    val infiniteTransition = rememberInfiniteTransition()
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing))
    )
    var imagePath: String

    if (pfp == "") {
        imagePath =
            "https://st3.depositphotos.com/6672868/13701/v/450/depositphotos_137014128-stock-illustration-user-profile-icon.jpg"
    } else {
        imagePath = pfp
    }

    AsyncImage(
        model = imagePath,
        contentDescription = "Profile Picture",
        modifier = Modifier
            .drawBehind {
                rotate(rotationAnimation.value) {
                    drawCircle(RainbowBorder, style = Stroke(20f))
                }
            }
            .size(100.dp)
            .clip(RoundedCornerShape(100.dp))
            .clickable {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        contentScale = ContentScale.Crop,
    )


}