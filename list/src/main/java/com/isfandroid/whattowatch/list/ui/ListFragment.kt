package com.isfandroid.whattowatch.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.isfandroid.core.R
import com.isfandroid.whattowatch.core.ui.adapter.paging.MultiLargePagingAdapter
import com.isfandroid.whattowatch.core.utils.Constants
import com.isfandroid.whattowatch.core.ui.adapter.paging.LoadingStateAdapter
import com.isfandroid.whattowatch.list.databinding.FragmentListBinding
import com.isfandroid.whattowatch.list.di.listModule
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class ListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListViewModel by viewModel()
    private val navArgs: ListFragmentArgs by navArgs()

    private val multiAdapter by lazy {
        MultiLargePagingAdapter(
            onItemClicked = { multi ->
                val action = ListFragmentDirections.actionListFragmentToDetailFragment(
                    multiId = multi.id,
                    mediaType = multi.mediaType ?: "Unknown",
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
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(listModule)

        initScreen()
        initClickListener()
        initData()
        observeData()
    }

    private fun initScreen() {
        with(binding) {
            // Refresh
            srlList.setOnRefreshListener(this@ListFragment)

            // Page Title
            tvTitle.text = when (navArgs.multiType) {
                Constants.TRENDING_MULTI -> "Trending This Week"
                Constants.NOW_PLAYING_MOVIES -> "Now Playing Movies"
                Constants.UPCOMING_MOVIES -> "Upcoming Movies"
                Constants.POPULAR_MOVIES -> "Popular Movies"
                Constants.TOP_RATED_MOVIES -> "Top Rated Movies"
                Constants.POPULAR_TV_SHOWS -> "Popular TV Shows"
                Constants.TOP_RATED_TV_SHOWS -> "Top Rated TV Shows"
                else -> "Full Lists"
            }

            // Lists RV Setup
            rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvItems.adapter = multiAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { multiAdapter.retry() }
            )

            // Lists Paging Setup
            multiAdapter.addLoadStateListener { loadState ->
                // When Search Results is Empty
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && multiAdapter.itemCount < 1) {
                    // Empty State for Search Results
                    showListsLoading(false)
                    binding.empty.root.visibility = View.VISIBLE
                    binding.empty.btnRetry.visibility = View.GONE
                    rvItems.visibility = View.GONE

                    binding.empty.tvTitle.text = getString(R.string.txt_msg_no_results)
                    binding.empty.ivIllustration.setImageResource(R.drawable.illustration_empty_data)
                } else {
                    when (loadState.refresh) {
                        // When Search Results is Loading
                        is LoadState.Loading -> {
                            // Loading State for Sticky Search Results
                            showListsLoading(true)
                            binding.empty.root.visibility = View.GONE
                            rvItems.visibility = View.GONE
                        }
                        else -> {
                            // Success State for Search Results
                            showListsLoading(false)
                            binding.empty.root.visibility = View.GONE
                            rvItems.visibility = View.VISIBLE

                            val error = when {
                                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                                else -> null
                            }
                            error?.let {
                                // Error State for Search Results
                                showListsLoading(false)
                                if (multiAdapter.itemCount == 0) {
                                    binding.empty.root.visibility = View.VISIBLE
                                    binding.empty.btnRetry.visibility = View.VISIBLE
                                    rvItems.visibility = View.GONE

                                    binding.empty.tvTitle.text = getString(R.string.txt_msg_unable_to_load_data)
                                    binding.empty.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initClickListener() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.empty.btnRetry.setOnClickListener { initData() }
    }

    private fun initData() {
        when (navArgs.multiType) {
            Constants.TRENDING_MULTI -> viewModel.getTrendingThisWeekPaging()
            Constants.NOW_PLAYING_MOVIES -> viewModel.getNowPlayingMoviesPaging()
            Constants.UPCOMING_MOVIES -> viewModel.getUpcomingMoviesPaging()
            Constants.POPULAR_MOVIES -> viewModel.getPopularMoviesPaging()
            Constants.TOP_RATED_MOVIES -> viewModel.getTopRatedMoviesPaging()
            Constants.POPULAR_TV_SHOWS -> viewModel.getPopularTVShowsPaging()
            Constants.TOP_RATED_TV_SHOWS -> viewModel.getTopRatedTVShowsPaging()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Multi List Data
                viewModel.multiLists.collectLatest {
                    if (it != null) multiAdapter.submitData(lifecycle, it)
                }
            }
        }
    }

    private fun showListsLoading(state: Boolean) {
        if (state) {
            binding.listsShimmer.root.visibility = View.VISIBLE
            binding.listsShimmer.root.startShimmer()
        } else {
            binding.listsShimmer.root.stopShimmer()
            binding.listsShimmer.root.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        initData()
        binding.srlList.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}