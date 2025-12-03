package com.midcores.silapan.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun BackButton(navigator: Navigator) {
    IconButton(onClick = { navigator.pop() }) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}