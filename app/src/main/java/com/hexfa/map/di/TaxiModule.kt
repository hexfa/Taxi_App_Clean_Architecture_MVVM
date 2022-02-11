package com.hexfa.map.di

import com.hexfa.map.data.remote.TaxiApi
import com.hexfa.map.data.repository.RemoteDataSource
import com.hexfa.map.data.repository.TaxisRepositoryImpl
import com.hexfa.map.domain.common.Constants.BASE_URL
import com.hexfa.map.domain.repository.TaxiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * object TaxiModule: for usage of Dependency Injection -> Dagger Hilt
 *
 * Creating SingleTon and provide them with Injection Later
 */
@Module
@InstallIn(SingletonComponent::class)
object TaxiModule {
    @Provides
    @Singleton
    fun provideRetrofitInstance(): TaxiApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaxiApi::class.java)

    @Provides
    @Singleton
    fun provideTaxisRepository(remoteDataSource: RemoteDataSource): TaxiRepository {
        return TaxisRepositoryImpl(remoteDataSource)
    }
}