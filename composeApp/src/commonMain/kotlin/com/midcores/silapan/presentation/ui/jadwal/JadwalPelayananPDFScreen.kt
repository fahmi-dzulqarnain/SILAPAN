package com.midcores.silapan.presentation.ui.jadwal

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.midcores.silapan.presentation.ui.component.BackButton
import com.midcores.silapan.presentation.ui.component.PDFViewer

class JadwalPelayananPDFScreen(
    private val bulan: String,
    private val pdfUrl: String
): Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column {
            TopAppBar(
                title = {
                    Text("Jadwal Pelayanan $bulan")
                },
                navigationIcon = {
                    BackButton(navigator)
                }
            )

            PDFViewer(pdfUrl)
        }
    }
}