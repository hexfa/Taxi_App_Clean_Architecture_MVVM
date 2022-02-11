package com.hexfa.map.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.hexfa.map.domain.common.NetworkResult
import com.hexfa.map.domain.models.TaxiResponse
import com.hexfa.map.domain.use_case.GetTaxisUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainViewModel class :
 * receiving data from GetTaxisUseCase with collecting from flow and
 * passing it to Ui As LiveData
 *
 *
 * GetTaxisUseCase is provided by DaggerHilt Injection
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    private val getTaxisUseCase: GetTaxisUseCase,
) : ViewModel() {
    private val _taxis : MutableStateFlow<NetworkResult<TaxiResponse>> = MutableStateFlow(NetworkResult.Loading())
    val taxis : StateFlow<NetworkResult<TaxiResponse>> =_taxis

    lateinit var mMap: GoogleMap
    var markers = HashMap<String, Marker>()

    /**
     * Need to Call getTaxis() in init block so when we Make ViewModel object we get data from remote
     */
    init {
        getTaxis()
    }

    /**
     * collecting data from GetTaxisUseCase
     */
    private fun getTaxis() = viewModelScope.launch {
        getTaxisUseCase.invoke().collect {
            _taxis.value = it
        }
    }

}