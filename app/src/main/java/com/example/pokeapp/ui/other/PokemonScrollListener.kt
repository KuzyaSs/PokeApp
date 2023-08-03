package com.example.pokeapp.ui.other

import android.widget.AbsListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PokemonScrollListener(private val onScrolled: (Unit) -> Unit) : RecyclerView.OnScrollListener() {
    private var _isScrolling = false
    private var loadNewPokemonListJob: Job? = null
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            _isScrolling = true
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val totalVisibleItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val isLastItem = firstVisibleItemPosition >= totalItems - totalVisibleItems

        if (isLastItem) {
            loadNewPokemonListJob?.cancel()
            loadNewPokemonListJob = MainScope().launch {
                delay(500)
                onScrolled(Unit)
            }
        }
    }
}