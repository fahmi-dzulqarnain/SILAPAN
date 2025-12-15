package com.midcores.silapan.presentation.ui.pengaduan

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ReportOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.midcores.silapan.domain.model.Pengaduan
import com.midcores.silapan.presentation.ui.component.EmptyState
import com.midcores.silapan.presentation.ui.theme.OnSecondaryContainer
import com.midcores.silapan.presentation.ui.theme.SecondaryContainer
import com.midcores.silapan.presentation.viewmodel.pengaduan.PengaduanViewModel
import com.midcores.silapan.presentation.viewmodel.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object PengaduanScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val viewModel: PengaduanViewModel = koinScreenModel()
        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadUserPengaduan()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Daftar Laporan") }
                )
            },
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                    PengaduanContent(
                        state = state,
                        onRefresh = { viewModel.loadUserPengaduanSync(true) },
                        isRefreshing = state.isLoading,
                        onCategoryClick = { viewModel.onCategorySelected(it) },
                        onDeleteItem = { viewModel.delete(it) },
                        onPengaduanClick = { item ->
                            viewModel.updatePengaduan(item)
                            navigator.push(PengaduanFormScreen)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PengaduanContent(
    state: UiState<Pengaduan>,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    onCategoryClick: (String) -> Unit,
    onDeleteItem: (String) -> Unit,
    onPengaduanClick: (Pengaduan) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 64.dp)
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.availableCategories) { category ->
                FilterChip(
                    selected = (category == state.selectedCategory),
                    onClick = { onCategoryClick(category) },
                    label = { Text(category) }
                )
            }
        }

        if (state.filteredData.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                EmptyState(
                    "Belum ada laporan pengaduan",
                    icon = Icons.Default.ReportOff
                )
            }
        } else {
            PullToRefreshBox(
                onRefresh = { onRefresh() },
                isRefreshing = isRefreshing
            ) {
                LazyColumn(
                    Modifier.padding(top = 16.dp)
                ) {
                    state.filteredData.forEach {
                        item {
                            Card(
                                colors = CardColors(
                                    containerColor = SecondaryContainer,
                                    contentColor = OnSecondaryContainer,
                                    disabledContainerColor = SecondaryContainer,
                                    disabledContentColor = OnSecondaryContainer
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 16.dp)
                                    .clickable {
                                        onPengaduanClick(it)
                                    }
                            ) {
                                PengaduanItemView(
                                    it,
                                    onDeleteItem
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PengaduanItemView(
    item: Pengaduan,
    onDeleteItem: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val dismissState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        positionalThreshold = { it * .25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = Color.Red,
                animationSpec = tween(durationMillis = 300)
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(end = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        onDismiss = {
            coroutineScope.launch {
                delay(300)
                onDeleteItem(item.id)
            }
        },
        content = {
            Card {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            item.status?.capitalize(Locale.current) ?: "Tidak Diketahui",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        )

                        Text(
                            item.displayableCreatedAt(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(Modifier.padding(4.dp))

                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    )
}

enum class PengaduanFilter(val label: String, val value: String) {
    ALL("Semua", "semua"),
    PENGADUAN("Pengaduan", "pengaduan"),
    SAMPAH_LIAR("Sampah Liar", "sampah_liar");

    companion object {
        fun forLabel(label: String) = entries.find { it.label == label }
        fun forValue(value: String) = entries.find { it.value == value }
    }
}