package com.example.broadcastmedia

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.broadcastmedia.databinding.FragmentCustomReceiverBinding

/**
 * Third screen (custom option): registers the custom broadcast receiver, then
 * broadcasts the text entered on the previous screen so the receiver gets it.
 */
class CustomReceiverFragment : Fragment() {

    private var _binding: FragmentCustomReceiverBinding? = null
    private val binding get() = _binding!!

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val text = intent.getStringExtra(EXTRA_TEXT).orEmpty()
            _binding?.tvReceived?.text = getString(R.string.received_format, text)
        }
    }
    private var registered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomReceiverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val context = requireContext()
        val filter = IntentFilter(ACTION_CUSTOM)
        if (Build.VERSION.SDK_INT >= 33) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            context.registerReceiver(receiver, filter)
        }
        registered = true

        val message = arguments?.getString("message").orEmpty()
        context.sendBroadcast(
            Intent(ACTION_CUSTOM)
                .setPackage(context.packageName)
                .putExtra(EXTRA_TEXT, message)
        )
    }

    override fun onStop() {
        super.onStop()
        if (registered) {
            requireContext().unregisterReceiver(receiver)
            registered = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ACTION_CUSTOM = "com.example.broadcastmedia.CUSTOM_BROADCAST"
        const val EXTRA_TEXT = "text"
    }
}
