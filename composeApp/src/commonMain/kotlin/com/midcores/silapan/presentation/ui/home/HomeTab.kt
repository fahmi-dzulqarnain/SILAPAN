package com.midcores.silapan.presentation.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideOrientation
import cafe.adriel.voyager.transitions.SlideTransition
import com.midcores.silapan.presentation.ui.component.AppBottomBar

object HomeTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Beranda"
            val icon = rememberVectorPainter(Icons.Filled.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Scaffold(
            bottomBar = {
                AppBottomBar()
            }
        ) {
            Navigator(HomeScreen) { navigator ->
                SlideTransition(
                    navigator = navigator,
                    orientation = SlideOrientation.Vertical
                )
            }
        }
    }
}