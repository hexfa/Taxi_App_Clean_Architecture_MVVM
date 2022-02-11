package com.hexfa.map.data.remote

import com.hexfa.map.domain.models.TaxiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * api service interface TaxiApi
 * &
 * Suspending functions are at the center of everything coroutines.
 * A suspending function is simply a function that can be paused and resumed at a later time.
 * They can execute a long running operation and wait for it to complete without blocking.
 */
interface TaxiApi {
    @GET(EndPoints.TAXIS)
    suspend fun getTaxis(): Response<TaxiResponse>
}