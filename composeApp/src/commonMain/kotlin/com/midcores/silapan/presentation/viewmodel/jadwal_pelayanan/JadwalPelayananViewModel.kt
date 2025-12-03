package com.midcores.silapan.presentation.viewmodel.jadwal_pelayanan

import com.midcores.silapan.domain.model.JadwalPelayanan
import com.midcores.silapan.domain.model.Pengaduan
import com.midcores.silapan.domain.usecase.jadwal_pelayanan.GetJadwalPelayananUsecase
import com.midcores.silapan.presentation.ui.pengaduan.PengaduanFilter
import com.midcores.silapan.presentation.viewmodel.BaseViewModel
import com.midcores.silapan.presentation.viewmodel.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class JadwalPelayananViewModel(
    private val getJadwalUseCase: GetJadwalPelayananUsecase
) : BaseViewModel() {

    private val _allJadwal = MutableStateFlow<List<JadwalPelayanan>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    private val _selectedYear = MutableStateFlow(getCurrentYearString())
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _allJadwal,
        _selectedYear,
        _isLoading,
        _error
    ) { allJadwal, selectedYear, isLoading, error ->
        UiState(
            isLoading = isLoading,
            selectedCategory = selectedYear,
            error = error,
            filteredData = allJadwal
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun loadByYear(year: String, isRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.update { true }
            try {
                val jadwal = getJadwalUseCase(year, isRefresh)
                _allJadwal.update {
                    jadwal.ifEmpty { emptyList() }
                }
                _error.update { null }
            } catch (e: Exception) {
                _error.update { e.message ?: "Failed to load jadwal" }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun updateFilter(year: String) {
        _selectedYear.update { year }
    }

    @OptIn(ExperimentalTime::class)
    private fun getCurrentYearString(): String {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.year.toString()
    }
}