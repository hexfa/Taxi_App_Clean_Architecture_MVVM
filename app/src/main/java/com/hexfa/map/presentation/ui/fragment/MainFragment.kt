package com.hexfa.map.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hexfa.map.databinding.FragmentMainBinding
import com.hexfa.map.domain.common.NetworkResult
import com.hexfa.map.domain.models.Poi
import com.hexfa.map.presentation.adapter.TaxiAdapter
import com.hexfa.map.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var taxisAdapter: TaxiAdapter
    private val profileTag = "MainActivity_Log"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        taxisObserver()
        swipeRefreshLayoutFunctionality()
        onClickEvents()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    /**
     * OnClick events function of :
     * floating button, Request button, whole item on RecyclerView
     */
    private fun onClickEvents() {
        binding.fbMap.setOnClickListener {
            val action = createActionForTaxi(null)
            findNavController().navigate(action)
        }

        taxisAdapter.setOnRequestButtonClickListener { taxi ->
            showSnackBar("Request to car : ${taxi.id}")
        }
        taxisAdapter.setonItemClickListener { taxi ->
            val action = createActionForTaxi(taxi)
            findNavController().navigate(action)
        }
    }

    private fun createActionForTaxi(taxi: Poi?): NavDirections {
        return MainFragmentDirections.actionMainFragmentToMapFragment(
            taxiId = taxi?.id ?: -1,
            taxiFleetType = taxi?.fleetType ?: "",
            taxiHeading = taxi?.heading?.toFloat() ?: -1f,
            taxiLatitude = taxi?.coordinate?.latitude?.toFloat() ?: -1f,
            taxiLongitude = taxi?.coordinate?.longitude?.toFloat() ?: -1f,
        )
    }

    /**
     * OnSwipeRefreshLayout refreshing event function :
     * requesting to manual Refresh -> Refreshing Data From Remote
     */
    private fun swipeRefreshLayoutFunctionality() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            manualRefresh()
        }
    }

    /**
     * Observing the data of LiveData that we receive from ViewModel
     * & use the list to keep our ui list updated with usage of Difficult
     */
    private fun taxisObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.taxis.collectLatest {response ->
                    when (response) {
                        is NetworkResult.Success -> {
                            hideProgressBar()
                            response.data?.let { taxisResponse ->
                                taxisAdapter.differ.submitList(taxisResponse.poiList)
                            }
                        }
                        is NetworkResult.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                Log.e(profileTag, "An error occurred: $message")
                            }
                        }
                        is NetworkResult.Loading -> {
                            showProgressBar()
                        }
                    }
                }
            }
        }
    }

    /**
     * Hiding progress bar for data in MainActivity
     */
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = false
    }

    /**
     * Showing progress bar for data in MainActivity
     */
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    /**
     * Requesting to manual Refresh -> Refreshing Data From Remote
     */
    private fun manualRefresh() {
        viewModel.onManualRefresh()
        showSnackBar("Data Refreshed")
    }

    /**
     * Create Sandbar with 1 button
     * @param text -> is SnackBars text
     */
    private fun showSnackBar(text: String) {
        val snackBar = Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_LONG
        )

        snackBar.setAction("OK") { // Call your action method here
            snackBar.dismiss()
        }
        snackBar.show()
    }

    /**
     * Setting up recyclerview
     */
    private fun setUpRecyclerView() {
        taxisAdapter = TaxiAdapter()
        binding.apply {
            taxisRecyclerView.adapter = taxisAdapter
            taxisRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}