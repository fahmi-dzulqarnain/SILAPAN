package com.midcores.silapan.presentation.viewmodel.pengaduan

import cafe.adriel.voyager.core.model.ScreenModel
import com.midcores.silapan.domain.model.Pengaduan
import com.midcores.silapan.domain.usecase.pengaduan.DeletePengaduanUseCase
import com.midcores.silapan.domain.usecase.pengaduan.GetPengaduanListUseCase
import com.midcores.silapan.presentation.ui.pengaduan.PengaduanFilter
import com.midcores.silapan.presentation.viewmodel.BaseViewModel
import com.midcores.silapan.presentation.viewmodel.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PengaduanViewModel(
    private val getPengaduanList: GetPengaduanListUseCase,
    private val deletePengaduan: DeletePengaduanUseCase
) : BaseViewModel(), ScreenModel {

    private val _allPengaduan = MutableStateFlow<List<Pengaduan>>(emptyList())
    private val _selectedCategory = MutableStateFlow("Semua")
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _allPengaduan,
        _selectedCategory,
        _isLoading,
        _error
    ) { allPengaduan, selectedCategory, isLoading, error ->

        val categories = PengaduanFilter.entries.map { it.label }

        val filtered = if (selectedCategory == "Semua") {
            allPengaduan
        } else {
            val selectedJenis = PengaduanFilter.forLabel(selectedCategory) ?: PengaduanFilter.PENGADUAN
            allPengaduan.filter { it.jenis == selectedJenis.value }
        }

        UiState(
            isLoading = isLoading,
            error = error,
            selectedCategory = selectedCategory,
            availableCategories = categories,
            filteredData = filtered
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    suspend fun loadUserPengaduan(refresh: Boolean = false) {
        _isLoading.update { true }
        try {
            val data = getPengaduanList(refresh)
            _allPengaduan.update {
                if (data.isNullOrEmpty()) emptyList()
                else data
            }
            _error.update { null }
        } catch (e: Exception) {
            _error.update { e.message ?: "Failed to fetch pengaduan" }
        } finally {
            _isLoading.update { false }
        }
    }

    fun loadUserPengaduanSync(refresh: Boolean = false) {
        viewModelScope.launch {
            loadUserPengaduan(refresh)
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.update { category }
    }

    private val _selectedPengaduan: MutableStateFlow<Pengaduan?> = MutableStateFlow(null)
    val selectedPengaduan = _selectedPengaduan.asStateFlow()

    fun updatePengaduan(newData: Pengaduan?) {
        _selectedPengaduan.value = newData
    }

    fun delete(id: String) {
        viewModelScope.launch {
            deletePengaduan(id)
            loadUserPengaduan(true)
        }
    }
}