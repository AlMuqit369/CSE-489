package com.example.broadcastmedia

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.broadcastmedia.databinding.FragmentVideoBinding

/** Plays one video inside the app. */
class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val controller = MediaController(requireContext())
        controller.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(controller)
        binding.videoView.setOnPreparedListener {
            _binding?.progress?.visibility = View.GONE
            it.start()
        }
        binding.videoView.setOnErrorListener { _, _, _ ->
            _binding?.progress?.visibility = View.GONE
            Toast.makeText(requireContext(), R.string.media_failed, Toast.LENGTH_LONG).show()
            true
        }
        binding.videoView.setVideoURI(Uri.parse(VIDEO_URL))
    }

    override fun onPause() {
        super.onPause()
        _binding?.videoView?.pause()
    }

    override fun onDestroyView() {
        _binding?.videoView?.stopPlayback()
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val VIDEO_URL =
            "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
    }
}
