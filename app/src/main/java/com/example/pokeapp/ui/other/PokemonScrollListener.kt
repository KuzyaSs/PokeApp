package com.example.pokeapp.ui.other

import android.util.Log
import android.widget.AbsListView
import androidx.recyclerview.widget.RecyclerView

class PokemonScrollListener(private val onScrolled: (Boolean) -> Unit) : RecyclerView.OnScrollListener() {
    private var _isScrolling = false
    val isScrolling get() = _isScrolling

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            _isScrolling = true
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        Log.d("MY_ON_SCROLLED", recyclerView.adapter?.itemCount.toString())

        val isAtLastItem = false
        onScrolled(isAtLastItem)
    }
}