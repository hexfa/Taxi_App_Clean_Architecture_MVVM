package com.hexfa.map.data.repository

import com.hexfa.map.domain.common.NetworkResult
import com.hexfa.map.domain.models.Coordinate
import com.hexfa.map.domain.models.Poi
import com.hexfa.map.domain.models.TaxiResponse
import com.hexfa.map.domain.repository.TaxiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * for testing use case we need instance of repository
 * &
 * for Creating fake repository which will simulate the behavior or our real repository we extend it
 * from TaxiRepository(the interface of Repository that will simulate the real repository attitude)
 * &
 * but for providing the data we don't use real data from remote(that is not we do in test)
 * and because of that we create our fake data here and send it to GetTaxisUseCaseTest and test it to Test that
 * sending and receiving data is working correctly
 */
class FakeTaxisRepository : TaxiRepository {
    companion object {
        //make fake data
        fun getData(taxResponse: TaxiResponse): NetworkResult.Success<TaxiResponse> {
            return NetworkResult.Success(data = taxResponse)
        }

        private val coordinate1 =
            Coordinate(latitude = 53.6843411185656, longitude = 10.034848053311075)
        private val coordinate2 =
            Coordinate(latitude = 53.62437730040252, longitude = 9.781565603041141)
        private val coordinate3 =
            Coordinate(latitude = 53.68867273056803, longitude = 9.92748058763996)
        private val poi1 = Poi(id = 282467,
            coordinate = coordinate1,
            fleetType = "POOLING",
            heading = 313.6428108968617)
        private val poi2 = Poi(id = 465146,
            coordinate = coordinate2,
            fleetType = "POOLING",
            heading = 218.88890911604426)
        private val poi3 = Poi(id = 604239,
            coordinate = coordinate3,
            fleetType = "TAXI",
            heading = 10.998014160452263)

        private val poiList: List<Poi> = listOf(poi1, poi2, poi3)
        private val taxiResponse = TaxiResponse(poiList)

        val network: NetworkResult<TaxiResponse> = getData(taxiResponse)
    }

    override suspend fun getTaxis(): Flow<NetworkResult<TaxiResponse>> {
        return flow {
            emit(network)
        }
    }
}