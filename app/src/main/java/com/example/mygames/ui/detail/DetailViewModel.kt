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

    fun getGameDetail(gameId: Int) {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = DetailUiState.Loading
            try {
                val result = gameRepository.getGameDetail(gameId)
                val isFavorite = gameRepository.isFavoriteGame(gameId)
                _uiState.value = DetailUiState.Loaded(result, isFavorite)
            } catch (e: Throwable) {
                _uiState.value = DetailUiState.LoadError(e.message ?: "Error")
            }
        }
    }

    fun toggleFavorite() {
        val loadedState = _uiState.value as? DetailUiState.Loaded
        loadedState?.apply {
            viewModelScope.launch(dispatcherProvider.io) {
                try {
                    if (isFavorite) {
                        gameRepository.deleteFavoriteGame(gameDetail.id)
                    } else {
                        gameRepository.addFavoriteGame(gameDetail)
                    }
                    _uiState.value = DetailUiState.Loaded(gameDetail, !isFavorite)
                } catch (e: Throwable) {
                    //TODO: Handle error
                }
            }
        }
    }
}

sealed class DetailUiState {
    class Loaded(val gameDetail: GameDetail, val isFavorite: Boolean) : DetailUiState()
    object Loading : DetailUiState()
    class LoadError(val message: String) : DetailUiState()
}