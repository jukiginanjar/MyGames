package com.example.mygames.ui.favorite

import com.example.mygames.data.DispatcherProvider
import com.example.mygames.data.model.Game
import com.example.mygames.data.repository.GameRepository
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class FavoriteViewModelTest {

    private val repository: GameRepository = mockk()

    private val dispatcherProvider: DispatcherProvider = mockk()

    private fun getViewModel(games: List<Game>): FavoriteViewModel {
        every { repository.getFavoriteGames() } returns flowOf(games)
        every { dispatcherProvider.io } returns UnconfinedTestDispatcher()
        return FavoriteViewModel(
            repository,
            dispatcherProvider
        )
    }

    @Test
    fun textGetFavoriteGames() = runTest {
        val sourceGames = listOf(
            Game(
                1, "Game 1", "2022-02-02", 4.5, "http://thumbnail"
            )
        )
        val vm = getViewModel(sourceGames)
        vm.getFavoriteGames()

        val state = vm.uiState.value as? FavoriteUiState.Loaded
        assertEquals(state?.games?.first()?.id, 1)
    }
}