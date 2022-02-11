package com.hexfa.map.data.repository

import com.hexfa.map.data.remote.TaxiApi
import javax.inject.Inject

/**
 * RemoteDataSource : receiving data from ApiService and passing it to repository
 * &
 * TaxiApi is provided by DaggerHilt Injection
 */
class RemoteDataSource @Inject constructor(private val taxiApi: TaxiApi) {
    suspend fun getFeature() =
        taxiApi.getTaxis()
}