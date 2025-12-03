package com.midcores.silapan.presentation.viewmodel.galeri

import com.midcores.silapan.domain.model.Galeri
import com.midcores.silapan.domain.usecase.galeri.GetGaleriUsecase
import com.midcores.silapan.presentation.viewmodel.BaseViewModel
import com.midcores.silapan.presentation.viewmodel.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val getGalleryUseCase: GetGaleriUsecase
) : BaseViewModel() {

    private val _allGalleries = MutableStateFlow<List<Galeri>>(emptyList())
    private val _selectedCategory = MutableStateFlow("Semua")
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _allGalleries,
        _selectedCategory,
        _isLoading,
        _error
    ) { allGalleries, selectedCategory, isLoading, error ->

        val categories = listOf("Semua") + allGalleries.mapNotNull { it.category }.distinct().sorted()

        val filtered = if (selectedCategory == "Semua") {
            allGalleries
        } else {
            allGalleries.filter { it.category == selectedCategory }
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

    fun loadGallery(refresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.update { true }
            try {
                val galleries = getGalleryUseCase(refresh)
                _allGalleries.update { galleries }
                _error.update { null }
            } catch (e: Exception) {
                _error.update { e.message ?: "Failed to fetch galleries" }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.update { category }
    }
}