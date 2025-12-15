package com.midcores.silapan.presentation.viewmodel.pengaduan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import com.midcores.silapan.api.model.CreatePengaduanDto
import com.midcores.silapan.domain.usecase.pengaduan.CreatePengaduanUseCase
import com.midcores.silapan.presentation.ui.pengaduan.PengaduanFilter
import com.midcores.silapan.presentation.utility.ImageCompressor
import com.midcores.silapan.presentation.viewmodel.BaseViewModel
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBase64
import io.github.ismoy.imagepickerkmp.domain.extensions.loadPainter
import io.github.ismoy.imagepickerkmp.domain.models.GalleryPhotoResult
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.ktor.util.decodeBase64Bytes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PengaduanFormViewModel(
    private val createPengaduan: CreatePengaduanUseCase
) : BaseViewModel() {

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var jenis by mutableStateOf(PengaduanFilter.PENGADUAN)
        private set
    var imagePath by mutableStateOf<String?>(null)
        private set
    var titleError by mutableStateOf<String?>(null)
    var descriptionError by mutableStateOf<String?>(null)

    var showImageButtons by mutableStateOf(false)
    var showCamera by mutableStateOf(false)
    var capturedPhoto by mutableStateOf<Painter?>(null)
    var capturedPhotoBase64 by mutableStateOf<ByteArray?>(null)
    var showGallery by mutableStateOf(false)

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    fun onTitleChanged(value: String) {
        title = value
        if (value.isNotBlank()) errorResolved()
    }

    fun onDescriptionChanged(value: String) {
        description = value
        if (value.isNotBlank()) errorResolved()
    }

    fun onJenisChanged(value: PengaduanFilter) {
        jenis = value
    }

    fun onPhotoSelected(
        photo: GalleryPhotoResult? = null,
        photoResult: PhotoResult? = null
    ) {
        if (photo != null) {
            photo.let {

                imagePath = it.fileName
                capturedPhoto = it.loadPainter()
                capturedPhotoBase64 = it.loadBase64().decodeBase64Bytes()
                showGallery = false
            }
        } else {
            photoResult?.let {
                capturedPhoto = it.loadPainter()
                capturedPhotoBase64 = it.loadBase64().decodeBase64Bytes()
                showCamera = false
            }
        }

        if (capturedPhotoBase64 != null) {
            errorResolved()
        }
    }

    private fun errorResolved() {
        titleError = null
        descriptionError = null
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun submitPengaduan(
        addressLabel: String,
        locationLatLong: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (title.isBlank()) {
            titleError = "Isi Judul Laporan"
            descriptionError = "Berikan nama laporan dengan jelas"
            return
        }

        if (description.isBlank()) {
            titleError = "Isi Deskripsi Laporan"
            descriptionError = "Ceritakan detail agar solusi terbaik dapat diberikan"
            return
        }

        if (locationLatLong.isEmpty()) {
            titleError = "Dapatkan Lokasi"
            descriptionError = "Tekan tombol dapatkan lokasi untuk mendapatkan informasi lokasi kejadian"
            return
        } else {
            errorResolved()
        }

        if (capturedPhotoBase64 == null) {
            titleError = "Foto Diperlukan"
            descriptionError = "Pilih gambar atau ambil gambar dengan kamera"
            return
        }

        _uiState.value = FormUiState(loading = true)

        try {
            capturedPhotoBase64?.let {
                val cleanJpegBytes = ImageCompressor.compress(it, quality = 80)
                val pengaduanDTO = CreatePengaduanDto(
                    title = title,
                    description = description,
                    jenis = jenis.value,
                    locationLatLong,
                    addressLabel,
                    cleanJpegBytes,
                    imagePath ?: Uuid.random().toString()
                )

                createPengaduan(pengaduanDTO)

                _uiState.value = FormUiState(success = true)
                onSuccess()
            }

            if (capturedPhotoBase64 == null) {
                onError("Foto Diperlukan")
                _uiState.value = FormUiState(error = "Foto Diperlukan")
            }
        } catch (e: Exception) {
            onError(e.message ?: "Gagal mengirim laporan")
            _uiState.value = FormUiState(error = e.message)
        }
    }
}

data class FormUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)