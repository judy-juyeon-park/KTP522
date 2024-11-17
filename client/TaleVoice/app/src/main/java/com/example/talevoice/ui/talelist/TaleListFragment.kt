package com.example.talevoice.ui.talelist

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import com.example.talevoice.R
import kotlinx.coroutines.launch

class TaleListFragment : Fragment() {

    companion object {
        fun newInstance() = TaleListFragment()
    }

    private val viewModel: TaleListViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_tale_list, container, false)

        viewModel.taleList.observe(viewLifecycleOwner) { taleList ->
            Log.d("TaleListFragment", "taleList update")
            Log.d("TaleListFragment", taleList.toString())
        }

        lifecycleScope.launch {
            viewModel.getTaleDetail("1").let {
                Log.d("TaleListFragment", it.toString())
            }
        }

        return rootView
    }

}