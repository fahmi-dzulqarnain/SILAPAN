package com.midcores.silapan.presentation.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import cafe.adriel.voyager.core.screen.Screen
import com.midcores.silapan.presentation.ui.component.SilapanHeader
import com.midcores.silapan.presentation.ui.home.MenuItem
import com.midcores.silapan.presentation.ui.home.MenuItemView
import com.midcores.silapan.presentation.viewmodel.profile.ProfileViewModel
import org.koin.compose.koinInject

object ProfileScreen: Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel: ProfileViewModel = koinInject()
        val state by viewModel.uiState.collectAsState()
        val menuItems = listOf(
            // MenuItem(null, "Profil User") { },
            MenuItem(
                null,
                "Keluar Akun",
                icon = Icons.AutoMirrored.Filled.Logout
            ) {
                viewModel.logout()
            }
        )

        LaunchedEffect(Unit) {
            viewModel.loadAppProfile()
        }

        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Profil SILAPAN") }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (state.error != null) {
                    Text(
                        text = state.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(Modifier.padding(24.dp)) {
                        state.singleData?.let { profile ->
                            item {
                                SilapanHeader(profile.title)

                                Text(
                                    "Sekilas Tentang SILAPAN",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 16.dp,bottom = 10.dp)
                                )

                                profile.description?.let {
                                    Text(
                                        it,
                                        style = LocalTextStyle.current.merge(
                                            TextStyle(lineHeight = 1.5.em)
                                        ),
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(bottom = 24.dp)
                                    )
                                }
                            }
                        }

                        items(menuItems, key = { it.title }) {
                            MenuItemView(it)
                        }

                        item {
                            Spacer(Modifier.height(128.dp))
                        }
                    }
                }
            }
        }
    }
}