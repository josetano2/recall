package edu.bluejack23_2.recall.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

//val Purple80 = Color(0xFFD0BCFF)
//val PurpleGrey80 = Color(0xFFCCC2DC)
//val Pink80 = Color(0xFFEFB8C8)
//
//val Purple40 = Color(0xFF6650a4)
//val PurpleGrey40 = Color(0xFF625b71)
//val Pink40 = Color(0xFF7D5260)

val TextLight = Color(0xFF0F0e10)
val BackgroundLight = Color(0xFFFAF9FB)
val PrimaryLight = Color(0xFF8268AC)
val SecondaryLight = Color(0xFFBBA6D9)
val AccentLight = Color(0xFF9976D0)

val TextDark = Color(0xFFF0EFF1)
val BackgroundDark = Color(0xFF050406)
val PrimaryDark = Color(0xFF6D5397)
val SecondaryDark = Color(0xFF3B2659)
val TertiaryDark = Color(0xFF9284A8)
val AccentDark = Color(0xFF512F89)

val ErrorBackground = Color(0xFFFCACAC)
val ErrorText = Color(0xFF)

val SuccessBackground = Color(0xFACFCB6)
val SuccessText = Color(0xF3F5B44)

val SnackbarBackground = Color(0xFFD9ABFF)
val SnackbarText = Color(0xFF5A00A5)

val TextWhite = Color(0xffeeeeee)
val DeepBlue = Color(0xff06164c)
val ButtonBlue = Color(0xff505cf3)
val DarkerButtonBlue = Color(0xff566894)
val LightRed = Color(0xfffc879a)
val AquaBlue = Color(0xff9aa5c4)
val OrangeYellow1 = Color(0xfff0bd28)
val OrangeYellow2 = Color(0xfff1c746)
val OrangeYellow3 = Color(0xfff4cf65)
val Beige1 = Color(0xfffdbda1)
val Beige2 = Color(0xfffcaf90)
val Beige3 = Color(0xfff9a27b)
val LightGreen1 = Color(0xff54e1b6)
val LightGreen2 = Color(0xff36ddab)
val LightGreen3 = Color(0xff11d79b)
val BlueViolet1 = Color(0xffaeb4fd)
val BlueViolet2 = Color(0xff9fa5fe)
val BlueViolet3 = Color(0xff8f98fd)

val BackgroundLightGradient = Brush.verticalGradient(
    colors = listOf(
        AccentLight.copy(alpha = 0.3f),
        AccentLight.copy(alpha = 0.2f),
        SecondaryLight.copy(alpha = 0.1f),
        BackgroundLight,
        BackgroundLight,
    ),
    startY = 0f,
    endY = 1000f
)

val RainbowBorder = Brush.sweepGradient(
    listOf(
        Color(0xFFFF938B),
        Color(0xFFFF938B),
        Color(0xFFFF9FF6),
        Color(0xFFA296FF),
        Color(0xFF8D90FF),
        Color(0xFF86F2FF),
        Color(0xFF9CFFB0),
        Color(0xFFFFFFB5),
        Color(0xFFFFC1A4),
        Color(0xFFFFB496),
        Color(0xFFFF938B)
    ),
)

