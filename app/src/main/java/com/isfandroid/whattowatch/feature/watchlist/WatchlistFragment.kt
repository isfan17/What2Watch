package com.isfandroid.whattowatch.feature.watchlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.isfandroid.whattowatch.R
import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.databinding.FragmentWatchlistBinding
import com.isfandroid.whattowatch.feature.adapter.MultiLargeAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WatchlistFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WatchlistViewModel by viewModel()

    private val multiAdapter by lazy {
        MultiLargeAdapter(
            onItemClicked = { multi ->
                val action = WatchlistFragmentDirections.actionWatchlistFragmentToDetailFragment(
                    multiId = multi.id,
                    mediaType = multi.mediaType ?: "Unknown",
                )
                findNavController().navigate(action)
            }
        )
    }

    private val watchlistCategoriesPopup by lazy { ListPopupWindow(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScreen()
        initData()
        initClickListener()
        observeData()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initScreen() {
        with(binding) {
            // Refresh
            srlWatchlist.setOnRefreshListener(this@WatchlistFragment)

            // Category
            tvCategory.text = getString(R.string.txt_all)
            watchlistCategoriesPopup.setAdapter(
                ArrayAdapter<Any?>(
                    requireContext(),
                    R.layout.item_watchlist_category,
                    arrayOf(
                        getString(R.string.txt_all),
                        getString(R.string.txt_movies),
                        getString(R.string.txt_tv_shows),
                    ),
                )
            )
            watchlistCategoriesPopup.anchorView = tvCategory
            watchlistCategoriesPopup.width = 350
            watchlistCategoriesPopup.verticalOffset = resources.getDimensionPixelSize(R.dimen.category_item_offset)
            watchlistCategoriesPopup.setBackgroundDrawable(
                resources.getDrawable(R.drawable.bg_rounded_filled_white_bordered, requireContext().theme)
            )
            watchlistCategoriesPopup.isModal = true

            // Watchlist Multi RV Setup
            rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvItems.adapter = multiAdapter
        }
    }

    private fun initData() {
        // Watchlist Multi
        viewModel.getOnWatchlistMulti()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Watchlist Data
                viewModel.watchLists.collectLatest {
                    val emptyDesc = when (binding.tvCategory.text) {
                        getString(R.string.txt_movies) -> getString(R.string.txt_msg_no_watchlist_movies)
                        getString(R.string.txt_tv_shows) -> getString(R.string.txt_msg_no_watchlist_tv_shows)
                        else -> getString(R.string.txt_msg_no_watchlist_multi)
                    }

                    when (it) {
                        is Status.Loading -> showWatchlistMultiLoading(true)
                        is Status.Error -> {
                            showWatchlistMultiLoading(false)
                            binding.rvItems.visibility = View.GONE
                            binding.empty.root.visibility = View.VISIBLE
                            binding.empty.tvDescription.visibility = View.GONE
                            binding.empty.btnRetry.visibility = View.VISIBLE

                            binding.empty.tvTitle.text = getString(R.string.txt_msg_unable_to_load_data)
                            binding.empty.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                        }
                        is Status.Success -> {
                            showWatchlistMultiLoading(false)
                            if (it.data?.isEmpty() == true) {
                                binding.rvItems.visibility = View.GONE
                                binding.empty.root.visibility = View.VISIBLE
                                binding.empty.btnRetry.visibility = View.GONE

                                binding.empty.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                binding.empty.tvDescription.text = emptyDesc
                                binding.empty.ivIllustration.setImageResource(R.drawable.illustration_empty_data)
                            }
                            else {
                                binding.rvItems.visibility = View.VISIBLE
                                binding.empty.root.visibility = View.GONE
                                multiAdapter.submitList(it.data)
                            }
                        }
                        else -> {
                            showWatchlistMultiLoading(false)
                            binding.rvItems.visibility = View.GONE
                            binding.empty.root.visibility = View.VISIBLE
                            binding.empty.btnRetry.visibility = View.GONE

                            binding.empty.tvTitle.text = getString(R.string.txt_msg_empty_data)
                            binding.empty.tvDescription.text = emptyDesc
                            binding.empty.ivIllustration.setImageResource(R.drawable.illustration_empty_data)
                        }
                    }
                }
            }
        }
    }

    private fun initClickListener() {
        binding.empty.btnRetry.setOnClickListener { viewModel.getOnWatchlistMulti() }
        binding.tvCategory.setOnClickListener {
            watchlistCategoriesPopup.show()
        }
        watchlistCategoriesPopup.setOnItemClickListener { _, _, position, _ ->
            when(position) {
                0 -> {
                    binding.tvCategory.text = getString(R.string.txt_all)
                    viewModel.getOnWatchlistMulti()
                }
                1 -> {
                    binding.tvCategory.text = getString(R.string.txt_movies)
                    viewModel.getOnWatchlistMovies()
                }
                2 -> {
                    binding.tvCategory.text = getString(R.string.txt_tv_shows)
                    viewModel.getOnWatchlistTVShows()
                }
            }
        }

    }

    private fun showWatchlistMultiLoading(state: Boolean) {
        if (state) {
            binding.rvItems.visibility = View.GONE
            binding.empty.root.visibility = View.GONE
            binding.watchlistMultiShimmer.root.visibility = View.VISIBLE
            binding.watchlistMultiShimmer.root.startShimmer()
        } else {
            binding.watchlistMultiShimmer.root.stopShimmer()
            binding.watchlistMultiShimmer.root.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        initData()
        binding.srlWatchlist.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}