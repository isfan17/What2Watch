package com.isfandroid.whattowatch.home.ui.tvshows

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
import com.isfandroid.whattowatch.feature.adapter.MultiSmallAdapter
import com.isfandroid.whattowatch.home.ui.HomeViewModel
import com.isfandroid.whattowatch.core.utils.Constants
import com.isfandroid.whattowatch.home.databinding.FragmentTvShowsBinding
import com.isfandroid.whattowatch.home.ui.HomeFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class TVShowsFragment: Fragment() {

    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        requireParentFragment().getViewModel<HomeViewModel>()
    }

    private val popularTVShowsAdapter by lazy {
        MultiSmallAdapter(
            onItemClicked = { tvShow ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    multiId = tvShow.id,
                    mediaType = Constants.MEDIA_TYPE_TV_SHOW,
                )
                findNavController().navigate(action)
            }
        )
    }

    private val topRatedTVShowsAdapter by lazy {
        MultiSmallAdapter(
            onItemClicked = { tvShow ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    multiId = tvShow.id,
                    mediaType = Constants.MEDIA_TYPE_TV_SHOW,
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
        _binding = FragmentTvShowsBinding.inflate(inflater, container, false)
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
            // Popular TV Shows RV Setup
            popularRV.tvTitle.text = getString(R.string.txt_popular_tv_shows)
            popularRV.rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularRV.rvItems.adapter = popularTVShowsAdapter

            // Top Rated TV Shows RV Setup
            topRatedRV.tvTitle.text = getString(R.string.txt_top_rated_tv_shows)
            topRatedRV.rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            topRatedRV.rvItems.adapter = topRatedTVShowsAdapter
        }
    }

    private fun initClickListener() {
        with(binding) {
            popularRV.btnSeeMore.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToListFragment(
                    multiType = Constants.POPULAR_TV_SHOWS
                )
                findNavController().navigate(action)
            }
            topRatedRV.btnSeeMore.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToListFragment(
                    multiType = Constants.TOP_RATED_TV_SHOWS
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Popular TV Shows
                launch {
                    viewModel.popularTVShows.collectLatest {
                        when (it) {
                            is Status.Loading -> showPopularTVShowsLoading(true)
                            is Status.Error -> {
                                showPopularTVShowsLoading(false)
                                if (popularTVShowsAdapter.itemCount == 0) {
                                    binding.popularRV.root.visibility = View.GONE
                                    binding.popularError.root.visibility = View.VISIBLE
                                    binding.popularError.tvDescription.visibility = View.GONE
                                    binding.popularError.btnRetry.visibility = View.GONE

                                    binding.popularError.tvTitle.text = getString(R.string.txt_msg_unable_to_load_newest_data, "Popular TV Shows")
                                    binding.popularError.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                }
                            }
                            is Status.Success -> {
                                showPopularTVShowsLoading(false)
                                binding.popularRV.root.visibility = View.VISIBLE
                                binding.popularError.root.visibility = View.GONE
                                popularTVShowsAdapter.submitList(it.data)
                            }
                            else -> {
                                showPopularTVShowsLoading(false)
                                binding.popularRV.root.visibility = View.GONE
                                binding.popularError.root.visibility = View.VISIBLE
                                binding.popularError.btnRetry.visibility = View.GONE

                                binding.popularError.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                binding.popularError.tvDescription.text = getString(R.string.txt_msg_no_popular_multi, "TV Shows")
                                binding.popularError.ivIllustration.setImageResource(R.drawable.illustration_empty_data)
                            }
                        }
                        viewModel.checkForErrors()
                    }
                }

                // Top Rated TV Shows
                launch {
                    viewModel.topRatedTVShows.collectLatest {
                        when (it) {
                            is Status.Loading -> showTopRatedTVShowsLoading(true)
                            is Status.Error -> {
                                showTopRatedTVShowsLoading(false)
                                if (topRatedTVShowsAdapter.itemCount == 0) {
                                    binding.topRatedRV.root.visibility = View.GONE
                                    binding.topRatedError.root.visibility = View.VISIBLE
                                    binding.topRatedError.tvDescription.visibility = View.GONE
                                    binding.topRatedError.btnRetry.visibility = View.GONE

                                    binding.topRatedError.tvTitle.text = getString(R.string.txt_msg_unable_to_load_newest_data, "Top Rated TV Shows")
                                    binding.topRatedError.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                }
                            }
                            is Status.Success -> {
                                showTopRatedTVShowsLoading(false)
                                binding.topRatedRV.root.visibility = View.VISIBLE
                                binding.topRatedError.root.visibility = View.GONE
                                topRatedTVShowsAdapter.submitList(it.data)
                            }
                            else -> {
                                showTopRatedTVShowsLoading(false)
                                binding.topRatedRV.root.visibility = View.GONE
                                binding.topRatedError.root.visibility = View.VISIBLE
                                binding.topRatedError.btnRetry.visibility = View.GONE

                                binding.topRatedError.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                binding.topRatedError.tvDescription.text = getString(R.string.txt_msg_no_top_rated_multi, "TV Shows")
                                binding.topRatedError.ivIllustration.setImageResource(R.drawable.illustration_empty_data)
                            }
                        }
                        viewModel.checkForErrors()
                    }
                }
            }
        }
    }

    private fun showPopularTVShowsLoading(state: Boolean) {
        if (state) {
            binding.popularShimmer.root.visibility = View.VISIBLE
            binding.popularShimmer.root.startShimmer()
        } else {
            binding.popularShimmer.root.stopShimmer()
            binding.popularShimmer.root.visibility = View.GONE
        }
    }

    private fun showTopRatedTVShowsLoading(state: Boolean) {
        if (state) {
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