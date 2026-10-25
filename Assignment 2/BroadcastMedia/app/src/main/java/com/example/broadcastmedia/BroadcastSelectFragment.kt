package com.example.broadcastmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.broadcastmedia.databinding.FragmentBroadcastSelectBinding

/** First screen: choose the type of broadcast, then go to the next screen. */
class BroadcastSelectFragment : Fragment() {

    private var _binding: FragmentBroadcastSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBroadcastSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            resources.getStringArray(R.array.broadcast_types)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapter
        binding.btnNext.setOnClickListener {
            val nav = findNavController()
            if (binding.spinnerType.selectedItemPosition == POSITION_CUSTOM) {
                nav.navigate(R.id.action_select_to_input)
            } else {
                nav.navigate(R.id.action_select_to_battery)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val POSITION_CUSTOM = 0
    }
}
