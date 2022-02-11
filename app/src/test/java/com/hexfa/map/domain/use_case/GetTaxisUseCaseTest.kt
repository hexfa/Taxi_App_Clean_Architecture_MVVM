package com.hexfa.map.domain.use_case


import com.google.common.truth.Truth.assertThat
import com.hexfa.map.data.repository.FakeTaxisRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


/**
 * GetTaxisUseCaseTest for provide Test functionality
 * &
 * Creating instance of fake repository which is simulating
 * the real repository with extending from our TaxiRepository interface and providing the fake data and passing it
 * to our getTaxisUseCase object to receive the data and test it
 */
class GetTaxisUseCaseTest {
    private lateinit var getTaxisUseCase: GetTaxisUseCase
    private lateinit var fakeTaxisRepository: FakeTaxisRepository

    /**
     * setUp function will be called before every test
     * to make new object of  our FakeTaxisRepository & GetTaxisUseCase
     * so tests will not effect on each other and work separately
     */
    @Before
    fun setUp() {
        fakeTaxisRepository = FakeTaxisRepository()
        getTaxisUseCase = GetTaxisUseCase(fakeTaxisRepository)
    }

    /**
     * testing received the list size to prof that our our sending and receiving functionalities works fine
     */
    @Test
    fun `test response list`() = runBlocking {
        val taxis = getTaxisUseCase.invoke().first()
        assertThat(taxis.data!!.poiList.size == 3).isTrue()
    }

    /**
     * testing with id of a item from list  to prof that our our sending and receiving functionalities works fine
     */
    @Test
    fun `test response list data check with id`() = runBlocking {
        val taxis = getTaxisUseCase.invoke().first()
        assertThat(taxis.data!!.poiList[0].id == 282467).isTrue()
    }

    /**
     * testing with feelType of a item from list  to prof that our our sending and receiving functionalities works fine
     */
    @Test
    fun `test response list data check with feelType`() = runBlocking {
        val taxis = getTaxisUseCase.invoke().first()
        assertThat(taxis.data!!.poiList[2].fleetType == "POOLING").isFalse()
    }

    /**
     * testing with coordinate of a item from list  to prof that our our sending and receiving functionalities works fine
     */
    @Test
    fun `test response list data check with coordinate`() = runBlocking {
        val taxis = getTaxisUseCase.invoke().first()
        val boolean: Boolean = taxis.data!!.poiList[1].coordinate.latitude == 53.62437730040252 &&
                taxis.data!!.poiList[1].coordinate.longitude == 9.781565603041141
        assertThat(boolean).isTrue()
    }
}