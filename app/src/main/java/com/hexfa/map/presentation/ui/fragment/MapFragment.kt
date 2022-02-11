package com.hexfa.map.presentation.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hexfa.map.R
import com.hexfa.map.databinding.FragmentMapBinding
import com.hexfa.map.domain.common.NetworkResult
import com.hexfa.map.domain.models.Coordinate
import com.hexfa.map.domain.models.Poi
import com.hexfa.map.presentation.viewmodel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * MainViewModel is provided by DaggerHilt Injection for MapActivity
 */
@AndroidEntryPoint
class MapFragment : Fragment() , GoogleMap.OnMarkerClickListener  {

    private lateinit var binding: FragmentMapBinding
    private val viewModel by viewModels<MapViewModel>()
    private val profileTag = "MapActivity_Log"
    private val args: MapFragmentArgs by navArgs()



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    private val callback = OnMapReadyCallback { googleMap ->
        viewModel.mMap = googleMap
        val firstLoc = LatLng(53.694865, 9.757589)
        viewModel.mMap.moveCamera(CameraUpdateFactory.newLatLng(firstLoc))
        viewModel.mMap.setOnMarkerClickListener(this)
        fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    /**
     * receiving data from viewModel as LiveData
     * and creating a marker on google map for each item
     */
    private fun fetchData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taxis.collectLatest { response ->
                    when (response) {
                        is NetworkResult.Success -> {
                            hideProgressBar()
                            response.data?.let { taxisResponse ->
                                taxisResponse.poiList.forEach {
                                    drawMarker(it)
                                }
                            }
                            checkForChosenTaxi()
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


    private fun checkForChosenTaxi() {
        val id = args.taxiId
        val fleetType = args.taxiFleetType
        val heading = args.taxiHeading.toDouble()
        val lat = args.taxiLatitude.toDouble()
        val long = args.taxiLongitude.toDouble()

        if (id != -1) {
            val coordinate = Coordinate(latitude = lat, longitude = long)
            drawMarker(
                Poi(
                    id = id,
                    fleetType = fleetType,
                    coordinate = coordinate,
                    heading = heading
                )
            )
            viewModel.mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lat, long),
                    17f
                ), null
            )
        }
    }

    /**
     * draw marker based on coordinate and depends on fleetType we draw marker as TAXI or POOLING
     * and creating markers list (for the time that we want to zoom on specific marker when it was
     * clicked on MainActivity.kt)
     */
    private fun drawMarker(it: Poi) {
        val lat = it.coordinate.latitude
        val lng = it.coordinate.longitude
        val type = it.fleetType
        val heading = it.heading
        val position = LatLng(lat, lng)
        //Create small Marker for Taxi
        val height = 100
        val width = 100
        val bitmapTaxi = BitmapFactory.decodeResource(resources, R.drawable.taxi)
        val smallBitmapTaxi = Bitmap.createScaledBitmap(bitmapTaxi, width, height, false)
        val smallerMarkerIconTaxi = BitmapDescriptorFactory.fromBitmap(smallBitmapTaxi)
        //Create small Marker for Pooling
        val bitmapPooling = BitmapFactory.decodeResource(resources, R.drawable.pooling)
        val smallBitmapPooling =
            Bitmap.createScaledBitmap(bitmapPooling, width + 100, height + 100, false)
        val smallerMarkerIconPooling = BitmapDescriptorFactory.fromBitmap(smallBitmapPooling)


        if (type == "TAXI") {
            viewModel.mMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(type)
                    .icon(smallerMarkerIconTaxi)
                    .rotation(heading.toFloat())
            ).also {
                it?.let {
                    viewModel.markers[it.id] = it
                }
            }
        } else {
            viewModel.mMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(type)
                    .icon(smallerMarkerIconPooling)
                    .rotation(heading.toFloat())
            ).also {
                it?.let {
                    viewModel.markers[it.id] = it
                }

            }
        }
    }

    /**
     * hiding progress bar for data on the map
     */
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    /**
     * showing progress bar for data on the map
     */
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    /**
     * the camera set to center on the map that marker has been clicked and opening the title of it &
     * zooming 17f on the specific area
     */
    override fun onMarkerClick(marker: Marker): Boolean {
        marker.showInfoWindow()
        viewModel.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 17f), null)
        openBottomSheetRequest()
        return true
    }

    /**
     * open bottom sheet fragment to ask user whether user wants to request for car or not
     */
    private fun openBottomSheetRequest() {
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, "BottomSheetDialogTag")
    }

}