package com.midcores.silapan.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.midcores.silapan.presentation.ui.theme.GreenBrand
import com.midcores.silapan.presentation.ui.theme.YellowBrand
import org.jetbrains.compose.resources.painterResource
import silapan.composeapp.generated.resources.Res
import silapan.composeapp.generated.resources.feature_image_silapan

@Composable
fun SilapanHeader(
    title: String = "SILAPAN",
    isShowImage: Boolean = true,
    subtitleColor: Color = GreenBrand,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (isShowImage) {
            Image(
                painter = painterResource(Res.drawable.feature_image_silapan),
                contentDescription = "feature image silapan",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        Text(
            title,
            style = MaterialTheme.typography.displayLarge,
            color = YellowBrand,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        Text(
            "SISTEM INFORMASI LAYANAN PERSAMPAHAN NONGSAKU BERSIH",
            style = MaterialTheme.typography.titleLarge.merge(
                TextStyle(lineHeight = 1.3.em)
            ),
            color = subtitleColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}