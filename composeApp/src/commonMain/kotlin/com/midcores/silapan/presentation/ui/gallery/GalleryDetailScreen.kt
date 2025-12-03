package com.midcores.silapan.presentation.ui.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.midcores.silapan.domain.model.Galeri
import io.kamel.image.KamelImage // A good KMP image loader
import io.kamel.image.asyncPainterResource

data class GalleryDetailScreen(val gallery: Galeri) : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Dialog state for full-screen image
        var showImageDialog by remember { mutableStateOf(false) }
        var selectedImageUrl by remember { mutableStateOf<String?>(null) }

        Box(modifier = Modifier.fillMaxSize()) {
            // Combine feature image + other images for the pager
            val allImages = remember(gallery) {
                listOfNotNull(gallery.featureImage) + gallery.images.map { it.imageURL }
            }
            val pagerState = rememberPagerState(pageCount = { allImages.size })

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            val imageUrl = allImages[page]
                            KamelImage(
                                resource = asyncPainterResource(data = imageUrl),
                                contentDescription = gallery.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        selectedImageUrl = imageUrl
                                        showImageDialog = true
                                    }
                            )
                        }

                        Row(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            repeat(allImages.size) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (pagerState.currentPage == index) Color.White else Color.Gray
                                        )
                                )
                            }
                        }
                    }
                }

                // --- 5. Text Content ---
                item {
                    // Category Chip
                    gallery.category?.let {
                        AssistChip(
                            onClick = { /* No-op */ },
                            label = { Text(it) },
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }

                item {
                    // Title
                    Text(
                        text = gallery.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                item {
                    // Description
                    gallery.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 28.sp
                            ),
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }

                item { Spacer(Modifier.height(80.dp)) }
            }

            FilledTonalIconButton(
                onClick = { navigator.pop() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        if (showImageDialog && selectedImageUrl != null) {
            Dialog(onDismissRequest = { showImageDialog = false }) {
                KamelImage(
                    resource = asyncPainterResource(data = selectedImageUrl!!),
                    contentDescription = "Full screen image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showImageDialog = false } // Dismiss on click
                )
            }
        }
    }
}