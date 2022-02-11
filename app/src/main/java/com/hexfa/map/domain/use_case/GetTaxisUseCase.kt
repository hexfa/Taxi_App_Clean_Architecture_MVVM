package com.hexfa.map.domain.use_case


import com.hexfa.map.domain.common.NetworkResult
import com.hexfa.map.domain.models.TaxiResponse
import com.hexfa.map.domain.repository.TaxiRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

/**
 * GetTaxisUseCase class is responsible to receiving data from repository and pass it to ViewModel
 * &
 * TaxiRepository is provided by DaggerHilt Injection
 */
class GetTaxisUseCase @Inject constructor(
    private val repository: TaxiRepository,
) {
    suspend operator fun invoke(): Flow<NetworkResult<TaxiResponse>> {
        return repository.getTaxis()
    }
}
