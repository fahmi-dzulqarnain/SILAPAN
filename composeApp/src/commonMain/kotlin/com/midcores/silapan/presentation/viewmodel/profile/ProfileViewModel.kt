package com.midcores.silapan.presentation.viewmodel.profile

import com.midcores.silapan.domain.model.AppProfile
import com.midcores.silapan.domain.usecase.auth.LogoutUsecase
import com.midcores.silapan.domain.usecase.profile.GetSilapanProfileUsecase
import com.midcores.silapan.domain.usecase.profile.GetUserProfileUsecase
import com.midcores.silapan.presentation.viewmodel.BaseViewModel
import com.midcores.silapan.presentation.viewmodel.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUsecase,
    private val getAppProfileUseCase: GetSilapanProfileUsecase,
    private val logoutUseCase: LogoutUsecase
) : BaseViewModel() {

    private val _appProfile = MutableStateFlow<AppProfile?>(null)
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _appProfile,
        _isLoading,
        _error
    ) { appProfile, isLoading, error ->
        UiState<AppProfile?>(
            isLoading = isLoading,
            error = error,
            singleData = appProfile
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )


    fun loadAppProfile(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.update { true }
            try {
                val profile = getAppProfileUseCase(isRefresh)

                _appProfile.update { profile }
                _error.update { null }
            } catch (e: Exception) {
                _error.update { e.message ?: "Failed to load profile" }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.update { true }

            try {
                val isSuccessful = logoutUseCase()

                if (!isSuccessful) {
                    _error.update { "Failed to logout" }
                }
            } catch (e: Exception) {
                _error.update { e.message ?: "Failed to logout" }
            } finally {
                _isLoading.update { false }
            }
        }
    }
}