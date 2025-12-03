package com.midcores.silapan.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.ExperimentalResourceApi
import silapan.composeapp.generated.resources.Lato_Bold
import silapan.composeapp.generated.resources.Lato_Italic
import silapan.composeapp.generated.resources.Lato_Regular
import silapan.composeapp.generated.resources.Res

@Composable
@OptIn(ExperimentalResourceApi::class)
fun latoFontFamily(): FontFamily {
    return FontFamily(
        Font(
            resource = Res.font.Lato_Regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.Lato_Italic,
            weight = FontWeight.Normal,
            style = FontStyle.Italic
        ),
        Font(
            resource = Res.font.Lato_Bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        )
        // Add other fonts like Lato-Black, Lato-Light etc. here
    )
}

// 2. Create a new Typography object using the Lato font
@Composable
fun appTypography(): Typography {
    return Typography(
        displayLarge = TextStyle(
            fontFamily = latoFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        ),
        titleLarge = TextStyle(
            fontFamily = latoFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        titleMedium = TextStyle(
            fontFamily = latoFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        titleSmall = TextStyle(
            fontFamily = latoFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = latoFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = latoFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = latoFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ),
        labelSmall = TextStyle(
            fontFamily = latoFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )
    )
}