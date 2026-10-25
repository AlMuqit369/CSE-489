package com.example.broadcastmedia

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.broadcastmedia.databinding.FragmentImageBinding
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/** Loads an image from the internet and scales it with a pinch gesture. */
class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    private val matrix = Matrix()
    private var bitmap: Bitmap? = null
    private var scale = 1f
    private lateinit var detector: ScaleGestureDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detector = ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(d: ScaleGestureDetector): Boolean {
                    val newScale = (scale * d.scaleFactor).coerceIn(MIN_ZOOM, MAX_ZOOM)
                    val factor = newScale / scale
                    scale = newScale
                    matrix.postScale(factor, factor, d.focusX, d.focusY)
                    binding.imageView.imageMatrix = matrix
                    return true
                }
            })
        binding.imageView.setOnTouchListener { v, event ->
            detector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP) v.performClick()
            true
        }
        binding.imageView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> fitImage() }

        if (bitmap == null) loadImage() else showBitmap()
    }

    private fun loadImage() {
        binding.progress.visibility = View.VISIBLE
        thread {
            val loaded = try {
                val conn = URL(IMAGE_URL).openConnection() as HttpURLConnection
                conn.connectTimeout = TIMEOUT_MS
                conn.readTimeout = TIMEOUT_MS
                conn.instanceFollowRedirects = true
                conn.inputStream.use { BitmapFactory.decodeStream(it) }
            } catch (e: Exception) {
                null
            }
            activity?.runOnUiThread {
                val b = _binding ?: return@runOnUiThread
                b.progress.visibility = View.GONE
                if (loaded == null) {
                    Toast.makeText(requireContext(), R.string.image_failed, Toast.LENGTH_LONG).show()
                } else {
                    bitmap = loaded
                    showBitmap()
                }
            }
        }
    }

    private fun showBitmap() {
        binding.imageView.setImageBitmap(bitmap)
        binding.progress.visibility = View.GONE
        fitImage()
    }

    /** Fit the whole image inside the view, centred (also resets zoom). */
    private fun fitImage() {
        val b = bitmap ?: return
        val view = _binding?.imageView ?: return
        if (view.width == 0 || view.height == 0) return
        val fit = minOf(view.width / b.width.toFloat(), view.height / b.height.toFloat())
        scale = 1f
        matrix.reset()
        matrix.postScale(fit, fit)
        matrix.postTranslate(
            (view.width - b.width * fit) / 2f,
            (view.height - b.height * fit) / 2f
        )
        view.imageMatrix = matrix
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val IMAGE_URL = "https://picsum.photos/id/237/1200/1200"
        private const val TIMEOUT_MS = 15000
        private const val MIN_ZOOM = 1f
        private const val MAX_ZOOM = 6f
    }
}
