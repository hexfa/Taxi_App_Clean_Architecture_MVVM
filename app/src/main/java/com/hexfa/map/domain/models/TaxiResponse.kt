package com.hexfa.map.domain.models

/**
 *  Set and create response model data type that we receive from TaxiApi.getTaxis()
 */
data class TaxiResponse(
    val poiList: List<Poi>,
)
