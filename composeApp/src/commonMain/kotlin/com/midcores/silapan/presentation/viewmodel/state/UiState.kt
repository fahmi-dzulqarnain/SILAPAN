package com.midcores.silapan.presentation.viewmodel.state

import com.midcores.silapan.domain.model.Galeri

data class UiState<out T>(
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedCategory: String = "Semua",
    val availableCategories: List<String> = listOf("Semua"),
    val filteredData: List<T> = emptyList(),
    val singleData: T? = null
)