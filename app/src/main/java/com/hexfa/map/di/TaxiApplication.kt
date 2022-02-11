package com.hexfa.map.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * TaxiApplication.kt that is extended from Application() class
 * &
 * using @HiltAndroidApp for purpose of Dagger Hilt Usage
 */
@HiltAndroidApp
class TaxiApplication : Application()