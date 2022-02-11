package com.hexfa.map.domain.models

/**
 * Set and create class Poi model data type
 */
data class Poi(
    val coordinate: Coordinate,
    val fleetType: String,
    val heading: Double,
    val id: Int,
)