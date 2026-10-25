package com.example.broadcastmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.broadcastmedia.databinding.FragmentCustomInputBinding

/** Second screen (custom option): takes plain text and passes it on. */
class CustomInputFragment : Fragment() {

    private var _binding: FragmentCustomInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString().trim()
            if (text.isEmpty()) {
                binding.etMessage.error = getString(R.string.error_empty)
            } else {
                findNavController().navigate(
                    R.id.action_input_to_receiver,
                    bundleOf("message" to text)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
