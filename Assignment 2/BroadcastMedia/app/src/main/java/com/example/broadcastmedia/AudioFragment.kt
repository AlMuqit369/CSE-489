package com.example.broadcastmedia

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.broadcastmedia.databinding.FragmentAudioBinding

/** Plays one audio file inside the app using MediaPlayer. */
class AudioFragment : Fragment() {

    private var _binding: FragmentAudioBinding? = null
    private val binding get() = _binding!!

    private var player: MediaPlayer? = null
    private var prepared = false
    private val handler = Handler(Looper.getMainLooper())

    private val tick = object : Runnable {
        override fun run() {
            val p = player
            if (p != null && prepared && p.isPlaying) {
                _binding?.seekBar?.progress = p.currentPosition
                handler.postDelayed(this, TICK_MS)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnPlay.setOnClickListener { play() }
        binding.btnPause.setOnClickListener { pause() }
        binding.btnStop.setOnClickListener { stop() }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(bar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser && prepared) player?.seekTo(progress)
            }

            override fun onStartTrackingTouch(bar: SeekBar) = Unit
            override fun onStopTrackingTouch(bar: SeekBar) = Unit
        })
        prepare()
    }

    private fun prepare() {
        binding.tvAudioStatus.setText(R.string.audio_preparing)
        val p = MediaPlayer()
        p.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        p.setOnPreparedListener {
            prepared = true
            _binding?.seekBar?.max = it.duration
            _binding?.tvAudioStatus?.setText(R.string.audio_ready)
        }
        p.setOnCompletionListener {
            _binding?.tvAudioStatus?.setText(R.string.audio_stopped)
            _binding?.seekBar?.progress = 0
        }
        p.setOnErrorListener { _, _, _ ->
            Toast.makeText(requireContext(), R.string.media_failed, Toast.LENGTH_LONG).show()
            true
        }
        try {
            p.setDataSource(AUDIO_URL)
            p.prepareAsync()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), R.string.media_failed, Toast.LENGTH_LONG).show()
        }
        player = p
    }

    private fun play() {
        val p = player ?: return
        if (!prepared) return
        p.start()
        binding.tvAudioStatus.setText(R.string.audio_playing)
        handler.post(tick)
    }

    private fun pause() {
        val p = player ?: return
        if (prepared && p.isPlaying) {
            p.pause()
            _binding?.tvAudioStatus?.setText(R.string.audio_paused)
        }
    }

    private fun stop() {
        val p = player ?: return
        if (!prepared) return
        p.pause()
        p.seekTo(0)
        binding.seekBar.progress = 0
        binding.tvAudioStatus.setText(R.string.audio_stopped)
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onDestroyView() {
        handler.removeCallbacks(tick)
        player?.release()
        player = null
        prepared = false
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val AUDIO_URL = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        private const val TICK_MS = 500L
    }
}
