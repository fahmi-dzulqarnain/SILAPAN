package com.midcores.silapan.presentation.ui.gallery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.midcores.silapan.domain.model.Galeri
import com.midcores.silapan.presentation.ui.component.BackButton
import com.midcores.silapan.presentation.viewmodel.galeri.GalleryViewModel
import com.midcores.silapan.presentation.viewmodel.state.UiState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.koinInject

object GalleryListScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: GalleryViewModel = koinInject()
        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadGallery()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Gallery") },
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
                    GalleryContent(
                        state = state,
                        onCategoryClick = { viewModel.onCategorySelected(it) },
                        onGalleryClick = { item ->
                            navigator.push(GalleryDetailScreen(item))
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun GalleryContent(
        state: UiState<Galeri>,
        onCategoryClick: (String) -> Unit,
        onGalleryClick: (Galeri) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
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
            }

            items(state.filteredData, key = { it.id }) { gallery ->
                GalleryListItemCard(
                    gallery = gallery,
                    onClick = { onGalleryClick(gallery) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }

    @Composable
    private fun GalleryListItemCard(
        gallery: Galeri,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Card(
            onClick = onClick,
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                KamelImage(
                    resource = asyncPainterResource(data = gallery.featureImage ?: ""),
                    contentDescription = gallery.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(MaterialTheme.shapes.medium)
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .weight(1f)
                ) {
                    gallery.category?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = gallery.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}