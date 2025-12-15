package com.midcores.silapan.presentation.ui.pengaduan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.midcores.silapan.api.ApiRoutes
import com.midcores.silapan.presentation.ui.component.BackButton
import com.midcores.silapan.presentation.viewmodel.LocationFormViewModel
import com.midcores.silapan.presentation.viewmodel.pengaduan.PengaduanFormViewModel
import com.midcores.silapan.presentation.viewmodel.pengaduan.PengaduanViewModel
import io.github.ismoy.imagepickerkmp.domain.config.CameraCaptureConfig
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.config.PermissionAndConfirmationConfig
import io.github.ismoy.imagepickerkmp.domain.models.CapturePhotoPreference
import io.github.ismoy.imagepickerkmp.domain.models.CompressionLevel
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

object PengaduanFormScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        val viewModel: PengaduanFormViewModel = koinInject()
        val pengaduanViewModel: PengaduanViewModel = koinScreenModel()
        val locationFormViewModel: LocationFormViewModel = koinScreenModel()
        val currentPengaduan by pengaduanViewModel.selectedPengaduan.collectAsState()
        var isSubmitEnabled by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            locationFormViewModel.updateAddressLabel("", "", "")
            currentPengaduan?.let {
                viewModel.onTitleChanged(it.title)
                viewModel.onDescriptionChanged(it.description ?: "")
                viewModel.onJenisChanged(
                    PengaduanFilter.forValue(it.jenis) ?:
                    PengaduanFilter.PENGADUAN
                )

                it.location?.let { location ->
                    val location = location.split(",")
                    val latitude = location.first()
                    val longitude = location.last()

                    locationFormViewModel.updateAddressLabel(
                        it.addressLabel ?: "",
                        latitude,
                        longitude
                    )
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        val label =
                            if(currentPengaduan == null) "Tambah Laporan"
                            else "Detail Laporan"

                        Text(
                            label,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        BackButton(navigator)
                    }
                )
            }
        ) { padding ->
            LaunchedEffect(Unit) {
                if (currentPengaduan == null) {
                    return@LaunchedEffect
                }

                val selectedCategory = pengaduanViewModel.uiState.value.selectedCategory
                val initialJenis = PengaduanFilter.forLabel(selectedCategory)

                if (initialJenis == PengaduanFilter.ALL) {
                    return@LaunchedEffect
                }

                viewModel.onJenisChanged(initialJenis ?: PengaduanFilter.PENGADUAN)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .padding(16.dp)
                ) {
                    item {
                        AnimatedVisibility(
                            viewModel.titleError != null
                        ) {
                            viewModel.titleError?.let {
                                ErrorView(it, viewModel.descriptionError)
                            }

                            locationFormViewModel.uiState.error?.let {
                                ErrorView("Location Problem", it)
                            }
                        }

                        val jenisList = listOf(
                            PengaduanFilter.PENGADUAN,
                            PengaduanFilter.SAMPAH_LIAR
                        )

                        Text(
                            "Jenis Laporan",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(jenisList) { category ->
                                FilterChip(
                                    selected = (category == viewModel.jenis),
                                    onClick = {
                                        if (currentPengaduan == null) {
                                            viewModel.onJenisChanged(category)
                                        }
                                    },
                                    label = { Text(category.label) }
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = viewModel.title,
                            onValueChange = { viewModel.onTitleChanged(it) },
                            label = { Text("Berikan nama laporan ini...") },
                            readOnly = currentPengaduan != null,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = viewModel.description,
                            onValueChange = { viewModel.onDescriptionChanged(it) },
                            label = { Text("Ceritakan pada kami apa yang terjadi...") },
                            readOnly = currentPengaduan != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = locationFormViewModel.uiState.addressLabel,
                                onValueChange = { },
                                label = { Text("Lokasi") },
                                readOnly = true,
                                modifier = Modifier.weight(1f),
                                maxLines = 2
                            )

                            if (currentPengaduan == null) {
                                Spacer(Modifier.width(12.dp))

                                TextButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            locationFormViewModel.requestLocation()
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Default.LocationOn,
                                            contentDescription = "Lokasi",
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .width(20.dp)
                                        )

                                        Text(
                                            "Dapatkan Lokasi",
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        if (currentPengaduan == null) {
                            FilledTonalButton(
                                onClick = {
                                    viewModel.showImageButtons = !viewModel.showImageButtons
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(corner = CornerSize(12.dp))
                            ) {
                                Text(
                                    "Pilih Gambar",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        } else {
                            currentPengaduan?.photo?.let {
                                Text(
                                    "Sebelum",
                                    style = MaterialTheme.typography.headlineMedium
                                )

                                Spacer(Modifier.width(8.dp))

                                AsyncImage(
                                    "${ApiRoutes.STORAGE}${it}",
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        currentPengaduan?.afterPhoto?.let {
                            Text(
                                "Setelah",
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(Modifier.width(8.dp))

                            AsyncImage(
                                "${ApiRoutes.STORAGE}${it}",
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }

                        AnimatedVisibility(
                            viewModel.showImageButtons,
                            enter = fadeIn() + expandVertically(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Row {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.showCamera = true
                                        viewModel.showImageButtons = false
                                        viewModel.capturedPhoto = null
                                        viewModel.capturedPhotoBase64 = null
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(corner = CornerSize(12.dp))
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Camera,
                                            contentDescription = "Camera",
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .width(20.dp)
                                        )

                                        Text(
                                            "Kamera",
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                    }
                                }

                                Spacer(Modifier.width(8.dp))

                                OutlinedButton(
                                    onClick = {
                                        viewModel.showGallery = true
                                        viewModel.showImageButtons = false
                                        viewModel.capturedPhoto = null
                                        viewModel.capturedPhotoBase64 = null
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(corner = CornerSize(12.dp))
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.PhotoAlbum,
                                            contentDescription = "Galeri",
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .width(20.dp)
                                        )

                                        Text(
                                            "Galeri",
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                    }
                                }
                            }
                        }

                        viewModel.capturedPhoto?.let { painter ->
                            Image(
                                painter = painter,
                                contentDescription = "Captured photo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .imePadding()
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    if (currentPengaduan == null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            Button(
                                enabled = isSubmitEnabled,
                                onClick = {
                                    val latitude = locationFormViewModel.uiState.latitude
                                    val longitude = locationFormViewModel.uiState.longitude
                                    val addressLabel = locationFormViewModel.uiState.addressLabel
                                    val latLongString =
                                        if (latitude.isNotEmpty() && longitude.isNotEmpty())
                                            "$latitude,$longitude"
                                        else
                                            ""

                                    isSubmitEnabled = false
                                    coroutineScope.launch {
                                        viewModel.submitPengaduan(
                                            addressLabel,
                                            latLongString,
                                            onSuccess = {
                                                navigator.pop()
                                                isSubmitEnabled = true
                                            },
                                            onError = { error ->
                                                viewModel.titleError = "Gagal Mengunggah!"
                                                viewModel.descriptionError = error
                                                isSubmitEnabled = true
                                            }
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(corner = CornerSize(12.dp))
                            ) {
                                Text(
                                    "Kirim Laporan",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }

                            if (!isSubmitEnabled) {
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                            }
                        }
                    }
                }

                if (viewModel.showGallery) {
                    GalleryPickerLauncher(
                        onPhotosSelected = { photos ->
                            viewModel.onPhotoSelected(photos.first())
                        },
                        onError = { viewModel.showGallery = false },
                        onDismiss = { viewModel.showGallery = false },
                        cameraCaptureConfig = CameraCaptureConfig(
                            preference = CapturePhotoPreference.BALANCED,
                            compressionLevel = CompressionLevel.MEDIUM,
                            permissionAndConfirmationConfig = PermissionAndConfirmationConfig(),
                        ),
                        allowMultiple = false,
                        enableCrop = true
                    )
                }

                if (viewModel.showCamera) {
                    ImagePickerLauncher(
                        config = ImagePickerConfig(
                            onPhotoCaptured = { result ->
                                viewModel.onPhotoSelected(photoResult = result)
                            },
                            onError = { viewModel.showCamera = false },
                            onDismiss = { viewModel.showCamera = false },
                            cameraCaptureConfig = CameraCaptureConfig(
                                compressionLevel = CompressionLevel.HIGH,
                                permissionAndConfirmationConfig = PermissionAndConfirmationConfig()
                            ),
                            directCameraLaunch = true,
                            enableCrop = true
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorView(
    title: String,
    message: String? = null
) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                RoundedCornerShape(8.dp)
            )
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Error,
                "Error",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(16.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Text(
                title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        message?.let { description ->
            Text(
                description,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 24.dp, top = 4.dp)
            )
        }
    }
}
