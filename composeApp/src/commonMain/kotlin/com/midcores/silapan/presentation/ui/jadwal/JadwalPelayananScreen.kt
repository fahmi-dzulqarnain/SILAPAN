package com.midcores.silapan.presentation.ui.jadwal

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.midcores.silapan.api.ApiRoutes
import com.midcores.silapan.domain.model.JadwalPelayanan
import com.midcores.silapan.presentation.ui.component.BackButton
import com.midcores.silapan.presentation.ui.component.DropdownSelector
import com.midcores.silapan.presentation.ui.theme.OnSecondaryContainer
import com.midcores.silapan.presentation.ui.theme.PrimaryContainer
import com.midcores.silapan.presentation.ui.theme.SecondaryContainer
import com.midcores.silapan.presentation.viewmodel.jadwal_pelayanan.JadwalPelayananViewModel
import com.midcores.silapan.presentation.viewmodel.state.UiState
import org.koin.compose.koinInject

object JadwalPelayananScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val viewModel: JadwalPelayananViewModel = koinInject()

        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadByYear(viewModel.getCurrentYearString())
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Jadwal Pelayanan") },
                    navigationIcon = { BackButton(navigator) }
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
                    JadwalPelayananContent(
                        state = state,
                        onFilterChange = {
                            viewModel.updateFilter(it)
                        },
                        onItemClick = {
                            val url = "${ApiRoutes.STORAGE}${it.filePath}"
                            navigator.push(JadwalPelayananPDFScreen(it.bulan, url))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun JadwalPelayananContent(
    state: UiState<JadwalPelayanan>,
    onFilterChange: (String) -> Unit,
    onItemClick: (JadwalPelayanan) -> Unit
) {
    LazyColumn(
        Modifier.padding(all = 16.dp)
    ) {
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.availableCategories) { category ->
                    FilterChip(
                        selected = (category == state.selectedCategory),
                        onClick = { onFilterChange(category) },
                        label = { Text(category) }
                    )
                }
            }
        }

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
                        .padding(vertical = 4.dp)
                        .clickable {
                            onItemClick(it)
                        }
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                it.bulan.capitalize(Locale.current),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(PrimaryContainer)
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            )

                            Text(
                                it.updatedAt,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(Modifier.padding(4.dp))

                        Text(
                            it.bulan.capitalize(Locale.current),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}