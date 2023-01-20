package com.example.mygames.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygames.data.DispatcherProvider
import com.example.mygames.data.model.Game
import com.example.mygames.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var searchText: String? = null
    private var nextPage: Int? = null
    private var games: List<Game> = emptyList()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Empty)
    val uiState: StateFlow<HomeUiState> = _uiState

    fun getGames(searchText: String) {
        this.searchText = searchText.takeIf { searchText.isNotEmpty() }

        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = HomeUiState.Loading
            try {
                val result = gameRepository.getGames(1, searchText)
                nextPage = result.nextPage
                games = result.items
                _uiState.value = HomeUiState.Loaded(games)
            } catch (e: Throwable) {
                _uiState.value = HomeUiState.LoadError(e.localizedMessage ?: "Error")
            }
        }
    }

    fun getNextGames() {
        if (_uiState.value == HomeUiState.NextLoading || nextPage == null) {
            return
        }

        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = HomeUiState.NextLoading
            try {
                val result = gameRepository.getGames(nextPage!!, searchText)
                nextPage = result.nextPage
                games = games + result.items
                _uiState.value = HomeUiState.NextLoaded(games)
            } catch (e: Throwable) {
                _uiState.value = HomeUiState.NextLoadError(e.localizedMessage ?: "Error")
            }
        }
    }
}

sealed class HomeUiState {
    object Empty : HomeUiState()
    class Loaded(val games: List<Game>) : HomeUiState()
    class NextLoaded(val games: List<Game>) : HomeUiState()
    object Loading : HomeUiState()
    object NextLoading : HomeUiState()
    class LoadError(val message: String) : HomeUiState()
    class NextLoadError(val message: String) : HomeUiState()
}