package com.example.mygames.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mygames.R
import com.example.mygames.databinding.FragmentHomeBinding
import com.example.mygames.ui.GameListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private val binding get() = _binding!!

    private var _binding: FragmentHomeBinding? = null

    private val listAdapter by lazy { buildGameListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        getGames()

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { handleUiState(it) }
        }
    }

    private fun handleUiState(state: HomeUiState) {
        when (state) {
            HomeUiState.Loading -> {
                binding.swipeRefresh.isRefreshing = true
            }
            HomeUiState.NextLoading -> {
                binding.swipeRefresh.isRefreshing = true
            }
            is HomeUiState.LoadError -> {
                hideLoadingIndicator()
                showError(state.message)
            }
            is HomeUiState.NextLoadError -> {
                hideLoadingIndicator()
                showError(state.message)
            }
            is HomeUiState.Loaded -> {
                hideLoadingIndicator()
                listAdapter.submitList(state.games) {
                    binding.rvGames.scrollToPosition(0)
                }
            }
            is HomeUiState.NextLoaded -> {
                hideLoadingIndicator()
                listAdapter.submitList(state.games)
            }
            HomeUiState.Empty -> {
                //TODO: Show UI for empty screen
            }
        }
    }

    private fun hideLoadingIndicator() {
        binding.swipeRefresh.isRefreshing = false
    }

    private fun showError(message: String) {
        activity?.apply {
            Snackbar.make(
                findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun buildGameListAdapter() = GameListAdapter(
        onClick = {

        },
        onLastItemReached = {
            viewModel.getNextGames()
        }
    )

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initViews() {
        val divider =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                context?.getDrawable(R.drawable.rv_divider)?.also { setDrawable(it) }
            }
        binding.rvGames.addItemDecoration(divider)
        binding.rvGames.adapter = listAdapter

        binding.swipeRefresh.setOnRefreshListener {
            getGames()
        }

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                activity?.dismissKeyboard()
                getGames()
                true
            } else {
                false
            }
        }
    }

    private fun getGames() {
        viewModel.getGames(searchText = binding.etSearch.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Activity.dismissKeyboard() {
        val inputMethodManager = getSystemService( Context.INPUT_METHOD_SERVICE ) as InputMethodManager
        if( inputMethodManager.isAcceptingText )
            inputMethodManager.hideSoftInputFromWindow( this.currentFocus?.windowToken, /*flags:*/ 0)
    }
}