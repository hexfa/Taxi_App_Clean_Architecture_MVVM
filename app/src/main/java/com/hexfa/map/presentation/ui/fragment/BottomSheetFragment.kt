package com.hexfa.map.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hexfa.map.databinding.BottomsheetFragmentBinding


/**
 * BottomSheetFragment class for asking the user that whether he/she wants to request for that Car
 */
class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomsheetFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomsheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNo.setOnClickListener {
            this.dismiss()
        }

        binding.btnYes.setOnClickListener {
            Toast.makeText(context, "Request sent", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }
    }
}