package com.midcores.silapan.presentation.ui.login

import com.midcores.silapan.domain.usecase.auth.LoginUsecase
import com.midcores.silapan.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUsecase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onPhoneChanged(value: String) {
        _uiState.value = _uiState.value.copy(phone = value)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun login() {
        val phone = _uiState.value.phone.trim()
        val password = _uiState.value.password.trim()

        if (phone.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "No HP dan Password wajib diisi")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = loginUseCase(phone, password)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    success = result,
                    error = if (!result) "Login gagal, periksa kembali No HP dan Password" else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Terjadi kesalahan saat login"
                )
            }
        }
    }
}