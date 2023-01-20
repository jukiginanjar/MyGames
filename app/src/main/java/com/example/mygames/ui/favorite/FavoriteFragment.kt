package com.example.mygames.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mygames.R
import com.example.mygames.databinding.FragmentFavoriteBinding
import com.example.mygames.ui.detail.DetailActivity
import com.example.mygames.ui.shared.GameListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private val viewModel: FavoriteViewModel by viewModels()

    private val binding get() = _binding!!

    private var _binding: FragmentFavoriteBinding? = null

    private val listAdapter by lazy { buildGameListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val divider =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                context?.getDrawable(R.drawable.rv_divider)?.also { setDrawable(it) }
            }
        binding.rvGames.addItemDecoration(divider)
        binding.rvGames.adapter = listAdapter

        viewModel.getFavoriteGames()

        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                handleUiState(it)
            }
        }
    }

    private fun handleUiState(state: FavoriteUiState) {
        when (state) {
            FavoriteUiState.Empty -> {
                //TODO: Show empty UI
            }
            is FavoriteUiState.LoadError -> {
                //TODO: Show error UI
            }
            is FavoriteUiState.Loaded -> {
                listAdapter.submitList(state.games)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buildGameListAdapter() = GameListAdapter(
        onClick = {
            activity?.apply {
                startActivity(Intent(this, DetailActivity::class.java).putExtra("game_id", it.id))
            }
        }
    )
}