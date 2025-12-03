package com.midcores.silapan.presentation.ui.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.midcores.silapan.presentation.ui.component.AppBottomBar

object ProfileTab: Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Profil"
            val icon = rememberVectorPainter(Icons.Filled.Person)

            return remember {
                TabOptions(
                    index = 2u,
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
            Navigator(ProfileScreen) { navigator ->
                SlideTransition(
                    navigator = navigator
                )
            }
        }
    }
}