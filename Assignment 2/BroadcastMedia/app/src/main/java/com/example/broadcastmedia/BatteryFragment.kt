package com.example.broadcastmedia

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.broadcastmedia.databinding.FragmentBatteryBinding

/** Second screen (battery option): shows the system battery broadcast. */
class BatteryFragment : Fragment() {

    private var _binding: FragmentBatteryBinding? = null
    private val binding get() = _binding!!

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = show(intent)
    }
    private var registered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBatteryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // ACTION_BATTERY_CHANGED is sticky: registerReceiver returns the latest value now.
        val sticky = requireContext().registerReceiver(
            receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        registered = true
        sticky?.let { show(it) }
    }

    override fun onStop() {
        super.onStop()
        if (registered) {
            requireContext().unregisterReceiver(receiver)
            registered = false
        }
    }

    private fun show(intent: Intent) {
        val b = _binding ?: return
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
        val percent = if (level >= 0 && scale > 0) level * 100 / scale else 0
        b.tvLevel.text = getString(R.string.battery_level_format, percent)

        val status = getString(
            when (intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                BatteryManager.BATTERY_STATUS_CHARGING -> R.string.status_charging
                BatteryManager.BATTERY_STATUS_DISCHARGING -> R.string.status_discharging
                BatteryManager.BATTERY_STATUS_FULL -> R.string.status_full
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> R.string.status_not_charging
                else -> R.string.status_unknown
            }
        )
        b.tvStatus.text = getString(R.string.battery_status_format, status)

        val power = getString(
            when (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)) {
                BatteryManager.BATTERY_PLUGGED_AC -> R.string.power_ac
                BatteryManager.BATTERY_PLUGGED_USB -> R.string.power_usb
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> R.string.power_wireless
                else -> R.string.power_battery
            }
        )
        b.tvPower.text = getString(R.string.battery_power_format, power)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
