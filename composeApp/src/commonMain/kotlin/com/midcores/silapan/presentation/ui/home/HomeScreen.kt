package com.midcores.silapan.presentation.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import coil3.compose.AsyncImage
import com.midcores.silapan.domain.model.Galeri
import com.midcores.silapan.presentation.ui.component.SilapanHeader
import com.midcores.silapan.presentation.ui.gallery.GalleryDetailScreen
import com.midcores.silapan.presentation.ui.gallery.GalleryListScreen
import com.midcores.silapan.presentation.ui.jadwal.JadwalPelayananScreen
import com.midcores.silapan.presentation.ui.pengaduan.PengaduanFilter
import com.midcores.silapan.presentation.ui.pengaduan.PengaduanTab
import com.midcores.silapan.presentation.ui.profile.ProfileTab
import com.midcores.silapan.presentation.ui.theme.Primary
import com.midcores.silapan.presentation.ui.theme.Secondary
import com.midcores.silapan.presentation.viewmodel.galeri.GalleryViewModel
import com.midcores.silapan.presentation.viewmodel.pengaduan.PengaduanViewModel
import org.koin.compose.koinInject

object HomeScreen: Screen {
    @Composable
    override fun Content() {
        val galleryVM: GalleryViewModel = koinInject()
        val galleryState by galleryVM.uiState.collectAsState()
        val tabNavigator: TabNavigator = LocalTabNavigator.current
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val pengaduanViewModel: PengaduanViewModel = koinScreenModel()

        val menus = listOf(
            MenuItem(1, "Profil SILAPAN") {
                tabNavigator.current = ProfileTab
            },
            MenuItem(2, "Jadwal Pelayanan") {
                navigator.push(JadwalPelayananScreen)
            },
            MenuItem(3, "Laporan Pengaduan") {
                pengaduanViewModel.onCategorySelected(PengaduanFilter.PENGADUAN.label)
                tabNavigator.current = PengaduanTab
            },
            MenuItem(4, "Laporan Sampah Liar") {
                pengaduanViewModel.onCategorySelected(PengaduanFilter.SAMPAH_LIAR.label)
                tabNavigator.current = PengaduanTab
            },
            MenuItem(5, "Bank Sampah Mengajar") {
                navigator.push(GalleryListScreen)
            },
            MenuItem(6, "Kios Bank Sampah") {
                navigator.push(GalleryListScreen)
            },
            MenuItem(7, "Pekan Bersih Nongsa") {
                navigator.push(GalleryListScreen)
            },
            MenuItem(8, "Produk Daur Ulang") {
                navigator.push(GalleryListScreen)
            },
        )

        LaunchedEffect(Unit) {
            galleryVM.loadGallery()
        }

        LazyColumn(Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 16.dp)
        ) {

            item {
                SilapanHeader(
                    "SILAPAN",
                    isShowImage = true,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(menus, key = { it.title }) {
                MenuItemView(it)
            }

            item {
                Spacer(Modifier.height(24.dp))

                Text(
                    "Galeri",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(16.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (galleryState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else if (galleryState.error != null) {
                        Text(
                            text = galleryState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyRow {
                            items(galleryState.filteredData.size) { index ->
                                val gallery = galleryState.filteredData[index]
                                GaleriItem(gallery, navigator)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(128.dp))
            }
        }
    }
}

data class MenuItem(
    val number: Int?,
    val title: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit
)

@Composable
fun MenuItemView(menuItem: MenuItem) {
    Row(
        Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .clickable(true, onClick = menuItem.onClick)
            .border(
                BorderStroke(1.dp, Secondary),
                RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (menuItem.icon != null) {
            Icon(
                Icons.AutoMirrored.Filled.Logout,
                contentDescription = menuItem.title
            )
        }

        menuItem.number?.let { number ->
            Box(
                Modifier
                    .clip(CircleShape)
                    .width(20.dp)
                    .height(20.dp)
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    number.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Text(
            menuItem.title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun GaleriItem(galeri: Galeri, navigator: Navigator) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(12.dp))
            .clickable {
                navigator.push(GalleryDetailScreen(galeri))
            },
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.DarkGray
        ),
    ) {
        Column(
            Modifier
                .width(256.dp)
        ) {
            galeri.featureImage?.let {
                AsyncImage(
                    it,
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    modifier = Modifier
                        .height(128.dp)
                        .width(256.dp)
                )
            }

            Text(
                galeri.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                )
            )

            galeri.category?.let {
                AssistChip(
                    onClick = { /* No-op */ },
                    label = {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                )
            }
        }
    }
}