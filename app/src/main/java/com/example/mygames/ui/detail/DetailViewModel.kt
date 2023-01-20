package com.example.mygames.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygames.data.DispatcherProvider
import com.example.mygames.data.model.GameDetail
import com.example.mygames.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    private var isFavorite = false

    fun getGameDetail(gameId: Int) {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = DetailUiState.Loading
            try {
                val result = gameRepository.getGameDetail(gameId)
                _uiState.value = DetailUiState.Loaded(result, isFavorite)
            } catch (e: Throwable) {
                _uiState.value = DetailUiState.LoadError(e.message ?: "Error")
            }
        }
    }

    fun toggleFavorite() {
        val loadedState = _uiState.value as? DetailUiState.Loaded
        loadedState?.apply {
            _uiState.value = DetailUiState.Loaded(gameDetail, !isFavorite)
        }
    }
}

sealed class DetailUiState {
    class Loaded(val gameDetail: GameDetail, val isFavorite: Boolean) : DetailUiState()
    object Loading : DetailUiState()
    class LoadError(val message: String) : DetailUiState()
}