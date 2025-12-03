package com.midcores.silapan.presentation.ui.pengaduan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideOrientation
import cafe.adriel.voyager.transitions.SlideTransition
import com.midcores.silapan.presentation.ui.component.AppBottomBar
import com.midcores.silapan.presentation.viewmodel.pengaduan.PengaduanViewModel

object PengaduanTab: Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Laporan"
            val icon = rememberVectorPainter(Icons.Filled.Report)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val pengaduanViewModel: PengaduanViewModel = koinScreenModel()

        Navigator(PengaduanScreen) { navigator ->
            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = navigator.lastItem == PengaduanScreen,
                    ) {
                        AppBottomBar()
                    }
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = navigator.lastItem == PengaduanScreen,
                    ) {
                        FloatingActionButton(
                            onClick = {
                                pengaduanViewModel.updatePengaduan(null)
                                navigator.push(PengaduanFormScreen)
                            },
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Tambah Pengaduan"
                            )
                        }
                    }
                }
            ) {
                SlideTransition(
                    navigator = navigator,
                    orientation = SlideOrientation.Horizontal
                )
            }
        }
    }
}