package com.isfandroid.whattowatch.feature.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.isfandroid.whattowatch.feature.home.movies.MoviesFragment
import com.isfandroid.whattowatch.feature.home.tvshows.TVShowsFragment

class HomeFragmentsAdapter(parentFragment: Fragment): FragmentStateAdapter(parentFragment) {

    private val fragments = listOf(
        MoviesFragment(),
        TVShowsFragment()
    )

    val fragmentNames = listOf(
        "Movies",
        "TV Shows"
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}