package com.example.mygames.ui.detail

import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.mygames.R
import com.example.mygames.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModels()

    private lateinit var binding: ActivityDetailBinding

    private var gameId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameId = intent.getIntExtra("game_id", 0)
        viewModel.getGameDetail(gameId)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        binding.ivFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
        setContentView(binding.root)

        setTitle(R.string.topbar_title_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is DetailUiState.Loaded -> {
                        handleUiState(state)
                    }
                    is DetailUiState.LoadError -> {
                        //TODO: Show error
                    }
                    DetailUiState.Loading -> {
                        //TODO: Show loading UI
                    }
                }
            }
        }
    }

    private fun handleUiState(state: DetailUiState.Loaded) {
        val detail = state.gameDetail
        binding.apply {
            tvTitle.text = detail.title
            tvDeveloper.text = detail.developer
            tvRating.text = String.format("%.1f", detail.rating)
            tvDescription.text = Html.fromHtml(
                detail.description, Html.FROM_HTML_MODE_LEGACY
            )
            tvPlayed.text =
                getString(R.string.detail_played_suffix, detail.playtime)
            tvReleaseDate.text =
                getString(R.string.release_date_prefix, detail.releaseDate)
            ivCover.load(detail.thumbnailUrl)
            ivFavorite.load(
                when (state.isFavorite) {
                    true -> R.drawable.ic_favorite_24
                    else -> R.drawable.ic_favorite_outline_24
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}