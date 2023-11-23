package com.isfandroid.whattowatch.feature.home.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.isfandroid.whattowatch.R
import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.databinding.FragmentMoviesBinding
import com.isfandroid.whattowatch.feature.adapter.MultiSmallAdapter
import com.isfandroid.whattowatch.feature.home.HomeFragmentDirections
import com.isfandroid.whattowatch.feature.home.HomeViewModel
import com.isfandroid.whattowatch.core.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MoviesFragment: Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        requireParentFragment().getViewModel<HomeViewModel>()
    }

    private val nowPlayingMovieAdapter by lazy {
        MultiSmallAdapter(
            onItemClicked = { movie ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    multiId = movie.id,
                    mediaType = Constants.MEDIA_TYPE_MOVIE,
                )
                findNavController().navigate(action)
            }
        )
    }

    private val upcomingMovieAdapter by lazy {
        MultiSmallAdapter(
            onItemClicked = { movie ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    multiId = movie.id,
                    mediaType = Constants.MEDIA_TYPE_MOVIE,
                )
                findNavController().navigate(action)
            }
        )
    }

    private val popularMovieAdapter by lazy {
        MultiSmallAdapter(
            onItemClicked = { movie ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    multiId = movie.id,
                    mediaType = Constants.MEDIA_TYPE_MOVIE,
                )
                findNavController().navigate(action)
            }
        )
    }

    private val topRatedMovieAdapter by lazy {
        MultiSmallAdapter(
            onItemClicked = { movie ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    multiId = movie.id,
                    mediaType = Constants.MEDIA_TYPE_MOVIE,
                )
                findNavController().navigate(action)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScreen()
        initClickListener()
        observeData()
    }

    private fun initScreen() {
        with(binding) {
            // Now Playing Movies RV Setup
            nowPlayingRV.tvTitle.text = getString(R.string.txt_now_playing_movies)
            nowPlayingRV.rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            nowPlayingRV.rvItems.adapter = nowPlayingMovieAdapter

            // Upcoming Movies RV Setup
            upcomingRV.tvTitle.text = getString(R.string.txt_upcoming_movies)
            upcomingRV.rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            upcomingRV.rvItems.adapter = upcomingMovieAdapter

            // Popular Movies RV Setup
            popularRV.tvTitle.text = getString(R.string.txt_popular_movies)
            popularRV.rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularRV.rvItems.adapter = popularMovieAdapter

            // Top Rated Movies RV Setup
            topRatedRV.tvTitle.text = getString(R.string.txt_top_rated_movies)
            topRatedRV.rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            topRatedRV.rvItems.adapter = topRatedMovieAdapter
        }
    }

    private fun initClickListener() {
        with(binding) {
            nowPlayingRV.btnSeeMore.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToListFragment(
                    multiType = Constants.NOW_PLAYING_MOVIES
                )
                findNavController().navigate(action)
            }
            upcomingRV.btnSeeMore.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToListFragment(
                    multiType = Constants.UPCOMING_MOVIES
                )
                findNavController().navigate(action)
            }
            popularRV.btnSeeMore.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToListFragment(
                    multiType = Constants.POPULAR_MOVIES
                )
                findNavController().navigate(action)
            }
            topRatedRV.btnSeeMore.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToListFragment(
                    multiType = Constants.TOP_RATED_MOVIES
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Now Playing Movies
                launch {
                    viewModel.nowPlayingMovies.collectLatest {
                        when (it) {
                            is Status.Loading -> showNowPlayingMoviesLoading(true)
                            is Status.Error -> {
                                showNowPlayingMoviesLoading(false)
                                if (nowPlayingMovieAdapter.itemCount == 0) {
                                    binding.nowPlayingRV.root.visibility = View.GONE
                                    binding.nowPlayingError.root.visibility = View.VISIBLE
                                    binding.nowPlayingError.tvDescription.visibility = View.GONE
                                    binding.nowPlayingError.btnRetry.visibility = View.GONE

                                    binding.nowPlayingError.tvTitle.text = getString(R.string.txt_msg_unable_to_load_newest_data, "Now Playing Movies")
                                    binding.nowPlayingError.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                }
                            }
                            is Status.Success -> {
                                showNowPlayingMoviesLoading(false)
                                binding.nowPlayingRV.root.visibility = View.VISIBLE
                                binding.nowPlayingError.root.visibility = View.GONE
                                nowPlayingMovieAdapter.submitList(it.data)
                            }
                            else -> {
                                showNowPlayingMoviesLoading(false)
                                binding.nowPlayingRV.root.visibility = View.GONE
                                binding.nowPlayingError.root.visibility = View.VISIBLE
                                binding.nowPlayingError.btnRetry.visibility = View.GONE

                                binding.nowPlayingError.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                binding.nowPlayingError.tvDescription.text = getString(R.string.txt_msg_no_now_playing_movies)
                                binding.nowPlayingError.ivIllustration.setImageResource(R.drawable.illustration_empty_data)
                            }
                        }
                        viewModel.checkForErrors()
                    }
                }

                // Upcoming Movies
                launch {
                    viewModel.upcomingMovies.collectLatest {
                        when (it) {
                            is Status.Loading -> showUpcomingMoviesLoading(true)
                            is Status.Error -> {
                                showUpcomingMoviesLoading(false)
                                if (upcomingMovieAdapter.itemCount == 0) {
                                    binding.upcomingRV.root.visibility = View.GONE
                                    binding.upcomingError.root.visibility = View.VISIBLE
                                    binding.upcomingError.tvDescription.visibility = View.GONE
                                    binding.upcomingError.btnRetry.visibility = View.GONE

                                    binding.upcomingError.tvTitle.text = getString(R.string.txt_msg_unable_to_load_newest_data, "Upcoming Movies")
                                    binding.upcomingError.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                }
                            }
                            is Status.Success -> {
                                showUpcomingMoviesLoading(false)
                                binding.upcomingRV.root.visibility = View.VISIBLE
                                binding.upcomingError.root.visibility = View.GONE
                                upcomingMovieAdapter.submitList(it.data)
                            }
                            else -> {
                                showUpcomingMoviesLoading(false)
                                binding.upcomingRV.root.visibility = View.GONE
                                binding.upcomingError.root.visibility = View.VISIBLE
                                binding.upcomingError.btnRetry.visibility = View.GONE

                                binding.upcomingError.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                binding.upcomingError.tvDescription.text = getString(R.string.txt_msg_no_upcoming_multi, "Movies")
                                binding.upcomingError.ivIllustration.setImageResource(R.drawable.illustration_empty_data)
                            }
                        }
                        viewModel.checkForErrors()
                    }
                }

                // Popular Movies
                launch {
                    viewModel.popularMovies.collectLatest {
                        when (it) {
                            is Status.Loading -> showPopularMoviesLoading(true)
                            is Status.Error -> {
                                showPopularMoviesLoading(false)
                                if (popularMovieAdapter.itemCount == 0) {
                                    binding.popularRV.root.visibility = View.GONE
                                    binding.popularError.root.visibility = View.VISIBLE
                                    binding.popularError.tvDescription.visibility = View.GONE
                                    binding.popularError.btnRetry.visibility = View.GONE

                                    binding.popularError.tvTitle.text = getString(R.string.txt_msg_unable_to_load_newest_data, "Popular Movies")
                                    binding.popularError.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                }
                            }
                            is Status.Success -> {
                                showPopularMoviesLoading(false)
                                binding.popularRV.root.visibility = View.VISIBLE
                                binding.popularError.root.visibility = View.GONE
                                popularMovieAdapter.submitList(it.data)
                            }
                            else -> {
                                showPopularMoviesLoading(false)
                                binding.popularRV.root.visibility = View.GONE
                                binding.popularError.root.visibility = View.VISIBLE
                                binding.popularError.btnRetry.visibility = View.GONE

                                binding.popularError.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                binding.popularError.tvDescription.text = getString(R.string.txt_msg_no_popular_multi, "Movies")
                                binding.popularError.ivIllustration.setImageResource(R.drawable.illustration_empty_data)
                            }
                        }
                        viewModel.checkForErrors()
                    }
                }

                // Top Rated Movies
                launch {
                    viewModel.topRatedMovies.collectLatest {
                        when (it) {
                            is Status.Loading -> showTopRatedMoviesLoading(true)
                            is Status.Error -> {
                                showTopRatedMoviesLoading(false)
                                if (topRatedMovieAdapter.itemCount == 0) {
                                    binding.topRatedRV.root.visibility = View.GONE
                                    binding.topRatedError.root.visibility = View.VISIBLE
                                    binding.topRatedError.tvDescription.visibility = View.GONE
                                    binding.topRatedError.btnRetry.visibility = View.GONE

                                    binding.topRatedError.tvTitle.text = getString(R.string.txt_msg_unable_to_load_newest_data, "Top Rated Movies")
                                    binding.topRatedError.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                }
                            }
                            is Status.Success -> {
                                showTopRatedMoviesLoading(false)
                                binding.topRatedRV.root.visibility = View.VISIBLE
                                binding.topRatedError.root.visibility = View.GONE
                                topRatedMovieAdapter.submitList(it.data)
                            }
                            else -> {
                                showTopRatedMoviesLoading(false)
                                binding.topRatedRV.root.visibility = View.GONE
                                binding.topRatedError.root.visibility = View.VISIBLE
                                binding.topRatedError.btnRetry.visibility = View.GONE

                                binding.topRatedError.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                binding.topRatedError.tvDescription.text = getString(R.string.txt_msg_no_top_rated_multi, "Movies")
                                binding.topRatedError.ivIllustration.setImageResource(R.drawable.illustration_empty_data)
                            }
                        }
                        viewModel.checkForErrors()
                    }
                }
            }
        }
    }

    private fun showNowPlayingMoviesLoading(state: Boolean) {
        if (state) {
            binding.nowPlayingRV.root.visibility = View.GONE
            binding.nowPlayingError.root.visibility = View.GONE

            binding.nowPlayingShimmer.root.visibility = View.VISIBLE
            binding.nowPlayingShimmer.root.startShimmer()
        } else {
            binding.nowPlayingShimmer.root.stopShimmer()
            binding.nowPlayingShimmer.root.visibility = View.GONE
        }
    }

    private fun showUpcomingMoviesLoading(state: Boolean) {
        if (state) {
            binding.upcomingRV.root.visibility = View.GONE
            binding.upcomingError.root.visibility = View.GONE

            binding.upcomingShimmer.root.visibility = View.VISIBLE
            binding.upcomingShimmer.root.startShimmer()
        } else {
            binding.upcomingShimmer.root.stopShimmer()
            binding.upcomingShimmer.root.visibility = View.GONE
        }
    }

    private fun showPopularMoviesLoading(state: Boolean) {
        if (state) {
            binding.popularRV.root.visibility = View.GONE
            binding.popularError.root.visibility = View.GONE

            binding.popularShimmer.root.visibility = View.VISIBLE
            binding.popularShimmer.root.startShimmer()
        } else {
            binding.popularShimmer.root.stopShimmer()
            binding.popularShimmer.root.visibility = View.GONE
        }
    }

    private fun showTopRatedMoviesLoading(state: Boolean) {
        if (state) {
            binding.topRatedRV.root.visibility = View.GONE
            binding.topRatedError.root.visibility = View.GONE

            binding.topRatedShimmer.root.visibility = View.VISIBLE
            binding.topRatedShimmer.root.startShimmer()
        } else {
            binding.topRatedShimmer.root.stopShimmer()
            binding.topRatedShimmer.root.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}