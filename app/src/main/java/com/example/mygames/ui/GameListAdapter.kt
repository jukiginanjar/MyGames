package com.example.mygames.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.mygames.R
import com.example.mygames.data.model.Game
import com.example.mygames.databinding.LayoutGameItemBinding

class GameListAdapter(
    private val onClick: (Game) -> Unit,
    private val onLastItemReached: () -> Unit
) :
    ListAdapter<Game, GameListAdapter.GameViewHolder>(FlowerDiffCallback) {

    class GameViewHolder(itemView: View, val onClick: (Game) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private var currentGame: Game? = null

        init {
            itemView.setOnClickListener {
                currentGame?.let {
                    onClick(it)
                }
            }
        }

        fun bind(game: Game) {
            currentGame = game

            LayoutGameItemBinding.bind(itemView).apply {
                tvTitle.text = game.title
                tvRating.text = String.format("%.1f", game.rating)
                tvDate.text = root.context.getString(R.string.release_date_prefix, game.releaseDate)
                ivCover.load(game.thumbnailUrl) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(25f))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view =
            LayoutGameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        return GameViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))

        val isLastPosition = (itemCount - position) == 1
        if (isLastPosition) {
            onLastItemReached.invoke()
        }
    }
}

object FlowerDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }
}