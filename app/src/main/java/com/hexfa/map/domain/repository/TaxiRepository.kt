package com.hexfa.map.domain.repository

import com.hexfa.map.domain.common.NetworkResult
import com.hexfa.map.domain.models.TaxiResponse
import kotlinx.coroutines.flow.Flow

/**
 * TaxiRepository interface will do simulate the TaxisRepositoryImpl.kt
 * the most important reason to make Interface of Repository is for make the code more testable
 * and add Testability between repository & UseCase
 */
interface TaxiRepository {
    suspend fun getTaxis(): Flow<NetworkResult<TaxiResponse>>
}