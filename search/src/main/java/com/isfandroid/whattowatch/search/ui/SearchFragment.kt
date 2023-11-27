package com.isfandroid.whattowatch.search.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.isfandroid.whattowatch.R
import com.isfandroid.whattowatch.feature.adapter.paging.LoadingStateAdapter
import com.isfandroid.whattowatch.feature.adapter.paging.MultiLargePagingAdapter
import com.isfandroid.whattowatch.search.databinding.FragmentSearchBinding
import com.isfandroid.whattowatch.search.di.searchModule
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private val multiAdapter by lazy {
        MultiLargePagingAdapter(
            onItemClicked = { multi ->
                val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(
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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(searchModule)

        initScreen()
        observeData()
        initClickListener()
    }

    private fun initScreen() {
        with(binding) {
            // Search Function
            binding.svSearchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.searchMulti(query)
                    binding.svSearchBar.clearFocus()
                    return true
                }
                override fun onQueryTextChange(newText: String?) = false
            })

            // Search Results RV Setup
            rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvItems.adapter = multiAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { multiAdapter.retry() }
            )

            // Search Results Paging Setup
            multiAdapter.addLoadStateListener { loadState ->
                // When Search Results is Empty
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && multiAdapter.itemCount < 1) {
                    // Empty State for Search Results
                    showSearchResultsLoading(false)
                    binding.llGuides.visibility = View.GONE
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
                            showSearchResultsLoading(true)
                            binding.llGuides.visibility = View.GONE
                            binding.empty.root.visibility = View.GONE
                            rvItems.visibility = View.GONE
                        }
                        else -> {
                            // Success State for Search Results
                            showSearchResultsLoading(false)
                            binding.llGuides.visibility = View.GONE
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
                                showSearchResultsLoading(false)
                                if (multiAdapter.itemCount == 0) {
                                    binding.llGuides.visibility = View.GONE
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

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Search Results
                viewModel.searchResults.collect {
                    if (it != null) multiAdapter.submitData(lifecycle, it)
                }
            }
        }
    }

    private fun initClickListener() {
        binding.empty.btnRetry.setOnClickListener {
            viewModel.searchMulti(binding.svSearchBar.query.toString())
        }
    }

    private fun showSearchResultsLoading(state: Boolean) {
        if (state) {
            binding.searchResultsShimmer.root.visibility = View.VISIBLE
            binding.searchResultsShimmer.root.startShimmer()
        } else {
            binding.searchResultsShimmer.root.stopShimmer()
            binding.searchResultsShimmer.root.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}