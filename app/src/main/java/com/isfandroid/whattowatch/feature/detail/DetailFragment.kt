package com.isfandroid.whattowatch.feature.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.isfandroid.whattowatch.R
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.databinding.FragmentDetailBinding
import com.isfandroid.whattowatch.databinding.PopupYoutubePlayerBinding
import com.isfandroid.whattowatch.core.utils.Constants
import com.isfandroid.whattowatch.core.utils.Helper
import com.isfandroid.whattowatch.core.utils.Helper.showToast
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var trailerPopupBinding: PopupYoutubePlayerBinding

    private val viewModel: DetailViewModel by viewModel()
    private val navArgs: DetailFragmentArgs by navArgs()

    private var objMultiId: Int? = null
    private var objMultiTitle: String? = null
    private var objMediaType: String? = null
    private var objIsOnWatchlist: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
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
        // Refresh
        binding.srlDetail.setOnRefreshListener(this@DetailFragment)
    }

    private fun initData() {
        // Get Data from Navigation
        objMultiId = navArgs.multiId
        objMediaType = navArgs.mediaType

        // Get Detail Data
        if (objMediaType == Constants.MEDIA_TYPE_MOVIE) {
            viewModel.getMovieDetail(objMultiId!!)
        } else {
            viewModel.getTVShowDetail(objMultiId!!)
        }
    }

    @SuppressLint("InflateParams")
    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Detail Data
                launch {
                    viewModel.multiDetail.collectLatest {
                        when (it) {
                            is com.isfandroid.whattowatch.core.data.Status.Loading -> {
                                showPageLoading(true)
                            }
                            is com.isfandroid.whattowatch.core.data.Status.Error -> {
                                showPageLoading(false)
                                if (binding.tvTitleDetail.text == null || binding.tvTitleDetail.text == "") {
                                    binding.error.root.visibility = View.VISIBLE
                                    binding.svContent.visibility = View.GONE
                                    binding.clButtons.visibility = View.GONE

                                    binding.error.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                    binding.error.tvDescription.text = getString(R.string.txt_msg_unable_to_load_newest_data, "Detail")
                                    binding.error.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                } else {
                                    showToast(getString(R.string.txt_msg_unable_to_load_newest_data, "Detail"))
                                }
                            }
                            is com.isfandroid.whattowatch.core.data.Status.Success -> {
                                showPageLoading(false)
                                binding.error.root.visibility = View.GONE
                                binding.svContent.visibility = View.VISIBLE
                                binding.clButtons.visibility = View.VISIBLE

                                if (it.data != null) setDetailData(it.data!!)
                            }
                            null -> {
                                showPageLoading(false)
                                if (binding.tvTitleDetail.text == null || binding.tvTitleDetail.text == "") {
                                    binding.error.root.visibility = View.VISIBLE
                                    binding.svContent.visibility = View.GONE
                                    binding.clButtons.visibility = View.GONE

                                    binding.error.tvTitle.text = getString(R.string.txt_msg_empty_data)
                                    binding.error.tvDescription.text = getString(R.string.txt_msg_unable_to_load_newest_data, "Detail")
                                    binding.error.ivIllustration.setImageResource(R.drawable.illustration_no_connection)
                                } else {
                                    showToast(getString(R.string.txt_msg_unable_to_load_newest_data, "Detail"))
                                }
                            }
                        }
                    }
                }

                // Trailers
                launch {
                    viewModel.trailers.collectLatest {
                        if (it != null) {
                            when (it) {
                                is com.isfandroid.whattowatch.core.data.Status.Loading -> showTrailerLoading(true)
                                is com.isfandroid.whattowatch.core.data.Status.Error -> {
                                    showTrailerLoading(false)
                                    showToast(getString(R.string.txt_msg_unable_to_load_data))
                                }
                                is com.isfandroid.whattowatch.core.data.Status.Success -> {
                                    showTrailerLoading(false)
                                    if (!it.data.isNullOrEmpty()) {
                                        val trailerVideo = it.data!!.find { trailer ->
                                            trailer.name.contains("Trailer")
                                        }
                                        val trailerVideoKey = trailerVideo?.key ?: it.data!!.first().key

                                        // YouTube Popup Setup
                                        trailerPopupBinding = PopupYoutubePlayerBinding.inflate(layoutInflater)
                                        trailerPopupBinding.apply {
                                            lifecycle.addObserver(youtubePlayer)
                                            val listener: YouTubePlayerListener =
                                                object : AbstractYouTubePlayerListener() {
                                                    override fun onReady(youTubePlayer: YouTubePlayer) {
                                                        super.onReady(youTubePlayer)
                                                        youTubePlayer.loadOrCueVideo(lifecycle, trailerVideoKey, 0f)
                                                    }
                                                }
                                            youtubePlayer.initialize(listener)

                                            // Show Trailer Dialog
                                            val dialog = Dialog(requireContext())
                                            dialog.setContentView(trailerPopupBinding.root)
                                            dialog.window?.setBackgroundDrawable(
                                                (AppCompatResources.getDrawable(
                                                    requireContext(),
                                                    R.color.dark_500
                                                ))
                                            )
                                            val lp = WindowManager.LayoutParams()
                                            lp.copyFrom(dialog.window?.attributes)
                                            lp.width = WindowManager.LayoutParams.MATCH_PARENT
                                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT

                                            dialog.window?.attributes = lp
                                            dialog.show()
                                        }
                                    } else {
                                        showToast(getString(R.string.txt_msg_trailer_unavailable))
                                    }
                                }
                            }
                        }
                    }
                }

                // Watchlist Action Result
                launch {
                    viewModel.watchlistActionResult.collect {
                        if (it != null) {
                            showToast(it)

                            // Get Detail Data
                            if (objMediaType == Constants.MEDIA_TYPE_MOVIE) {
                                viewModel.getMovieDetail(objMultiId!!)
                            } else {
                                viewModel.getTVShowDetail(objMultiId!!)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initClickListener() {
        with(binding) {
            error.btnRetry.setOnClickListener { initData() }
            btnBack.setOnClickListener { findNavController().navigateUp() }
            btnShare.setOnClickListener { shareDetail() }
            btnPlayTrailer.setOnClickListener {
                if (objMediaType == Constants.MEDIA_TYPE_MOVIE) {
                    viewModel.getMovieTrailers(objMultiId!!)
                } else {
                    viewModel.getTVShowTrailers(objMultiId!!)
                }
            }
            btnWatchlist.setOnClickListener {
                if (objIsOnWatchlist!!) {
                    viewModel.removeMultiFromWatchlist(objMultiId!!, objMediaType!!)
                } else {
                    viewModel.addMultiToWatchlist(objMultiId!!, objMediaType!!)
                }
            }
        }
    }

    private fun setDetailData(data: Multi) {
        with(binding) {
            objMultiTitle = data.title ?: data.name
            objIsOnWatchlist = data.isOnWatchlist

            val categories = data.genres?.joinToString(", ")
            val time = when {
                data.runtime != null -> {
                    Helper.formatMinutesToHoursAndMinutes(data.runtime!!)
                }
                data.episodeRunTime != null -> {
                    if (data.episodeRunTime!!.isNotEmpty()) {
                        Helper.formatMinutesToHoursAndMinutes(data.episodeRunTime!!.sum())
                    } else "Unknown"
                }
                else -> "Unknown"
            }
            val watchlistText = if (data.isOnWatchlist) {
                getString(R.string.txt_remove_from_watchlist)
            } else {
                getString(R.string.txt_add_to_watchlist)
            }
            tvQuote.visibility = if (data.tagline == null || data.tagline == "") View.GONE else View.VISIBLE

            Glide.with(this@DetailFragment)
                .load("${Constants.IMAGE_BASE_URL}${data.backdropPath}")
                .placeholder(R.drawable.placeholder_item_multi)
                .into(ivCover)
            tvTitleDetail.text = data.title ?: data.name
            tvQuote.text = data.tagline
            tvCategories.text = categories
            tvRating.text = String.format("%.1f", data.voteAverage)
            tvTime.text = time
            tvDate.text = data.releaseDate ?: data.firstAirDate
            tvOverview.text = data.overview
            btnWatchlist.text = watchlistText
        }
    }

    private fun showPageLoading(state: Boolean) {
        if (state) {
            binding.svContent.visibility = View.GONE
            binding.clButtons.visibility = View.GONE
            binding.error.root.visibility = View.GONE

            binding.shimmerLoading.root.visibility = View.VISIBLE
            binding.shimmerLoading.root.startShimmer()
        } else {
            binding.shimmerLoading.root.stopShimmer()
            binding.shimmerLoading.root.visibility = View.GONE
        }
    }

    private fun showTrailerLoading(state: Boolean) {
        if (state) {
            binding.btnPlayTrailer.text = null
            binding.progressBarTrailer.visibility = View.VISIBLE
        } else {
            binding.btnPlayTrailer.text = getString(R.string.txt_play_trailer)
            binding.progressBarTrailer.visibility = View.GONE
        }
    }

    private fun shareDetail() {
        val shareText = when (objMediaType) {
            Constants.MEDIA_TYPE_MOVIE -> getString(R.string.txt_share_movie_detail, objMultiTitle)
            else -> getString(R.string.txt_share_tv_show_detail, objMultiTitle)
        }

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.type = "text/plain"

        startActivity(sendIntent)
    }

    override fun onRefresh() {
        initData()
        binding.srlDetail.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}