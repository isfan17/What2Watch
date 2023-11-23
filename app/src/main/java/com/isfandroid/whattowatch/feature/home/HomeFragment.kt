package com.isfandroid.whattowatch.feature.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.isfandroid.whattowatch.R
import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.databinding.FragmentHomeBinding
import com.isfandroid.whattowatch.feature.adapter.HomeFragmentsAdapter
import com.isfandroid.whattowatch.feature.adapter.MultiSmallAdapter
import com.isfandroid.whattowatch.core.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var fragmentsAdapter: HomeFragmentsAdapter

    private val multiAdapter by lazy {
        MultiSmallAdapter(
            onItemClicked = { multi ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScreen()
        initClickListener()
        initData()
        observeData()
    }

    private fun initScreen() {
        with(binding) {
            // Refresh
            srlHome.setOnRefreshListener(this@HomeFragment)

            // Fragment Adapter
            fragmentsAdapter = HomeFragmentsAdapter(this@HomeFragment)
            viewPager.adapter = fragmentsAdapter
            viewPager.isUserInputEnabled = false

            // Attaching viewpager to tab layout
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = (viewPager.adapter as HomeFragmentsAdapter).fragmentNames[position]
            }.attach()

            // Trending Multi RV Setup
            trendingMultiRV.tvTitle.text = getString(R.string.txt_trending_this_week)
            trendingMultiRV.rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            trendingMultiRV.rvItems.adapter = multiAdapter
        }
    }

    private fun initData() {
        // Trending Multi
        viewModel.getTrendingMulti()

        // Movies
        viewModel.getNowPlayingMovies()
        viewModel.getUpcomingMovies()
        viewModel.getPopularMovies()
        viewModel.getTopRatedMovies()

        // TV Shows
        viewModel.getPopularTVShows()
        viewModel.getTopRatedTVShows()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Error State
                launch {
                    viewModel.hasError.collectLatest {
                        if (it != null) {
                            binding.error.tvMessageError.text = getString(R.string.txt_msg_unable_to_load_data)
                            if (it) binding.error.root.visibility = View.VISIBLE
                            else binding.error.root.visibility = View.GONE
                        }
                    }
                }

                // Trending Multi
                launch {
                    viewModel.trendingMulti.collectLatest {
                        when (it) {
                            is Status.Loading -> showTrendingMultiLoading(true)
                            is Status.Error -> {
                                showTrendingMultiLoading(false)
                                if (multiAdapter.itemCount == 0) {
                                    binding.trendingMultiRV.root.visibility = View.GONE
                                    binding.trendingMultiError.root.visibility = View.VISIBLE
                                    binding.trendingMultiError.tvDescription.visibility = View.GONE
                                    binding.trendingMultiError.btnRetry.visibility = View.VISIBLE

                                    binding.trendingMultiError.tvTitle.text = getString(R.string.txt_msg_unable_to_load_newest_data, "Trending")
                                    binding.trendingMultiError.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                }
                            }
                            is Status.Success -> {
                                showTrendingMultiLoading(false)
                                binding.trendingMultiRV.root.visibility = View.VISIBLE
                                binding.trendingMultiError.root.visibility = View.GONE
                                multiAdapter.submitList(it.data)
                            }
                            else -> {
                                showTrendingMultiLoading(false)
                                binding.trendingMultiRV.root.visibility = View.GONE
                                binding.trendingMultiError.root.visibility = View.VISIBLE
                                binding.trendingMultiError.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                binding.trendingMultiError.tvDescription.text = getString(R.string.txt_msg_no_trending_multi)
                                binding.trendingMultiError.btnRetry.visibility = View.GONE
                            }
                        }
                        viewModel.checkForErrors()
                    }
                }
            }
        }
    }

    private fun initClickListener() {
        binding.error.btnRetryError.setOnClickListener { initData() }
        binding.trendingMultiError.btnRetry.setOnClickListener { viewModel.getTrendingMulti() }
        binding.trendingMultiRV.btnSeeMore.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToListFragment(
                multiType = Constants.TRENDING_MULTI
            )
            findNavController().navigate(action)
        }
    }

    private fun showTrendingMultiLoading(state: Boolean) {
        if (state) {
            binding.trendingMultiRV.root.visibility = View.GONE
            binding.trendingMultiError.root.visibility = View.GONE

            binding.trendingMultiShimmer.root.visibility = View.VISIBLE
            binding.trendingMultiShimmer.root.startShimmer()
        } else {
            binding.trendingMultiShimmer.root.stopShimmer()
            binding.trendingMultiShimmer.root.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        initData()
        binding.srlHome.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}