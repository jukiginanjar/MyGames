package com.example.mygames.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygames.data.DispatcherProvider
import com.example.mygames.data.model.Game
import com.example.mygames.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Empty)
    val uiState: StateFlow<FavoriteUiState> = _uiState

    fun getFavoriteGames() {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                gameRepository.getFavoriteGames()
                    .distinctUntilChanged()
                    .collectLatest {
                        if (it.isNotEmpty()) {
                            _uiState.value = FavoriteUiState.Loaded(it)
                        } else {
                            _uiState.value = FavoriteUiState.Empty
                        }
                    }
            } catch (e: Throwable) {
                _uiState.value = FavoriteUiState.LoadError(e.message ?: "Error")
            }
        }
    }
}

sealed class FavoriteUiState {
    object Empty : FavoriteUiState()
    class Loaded(val games: List<Game>) : FavoriteUiState()
    class LoadError(val message: String) : FavoriteUiState()
}