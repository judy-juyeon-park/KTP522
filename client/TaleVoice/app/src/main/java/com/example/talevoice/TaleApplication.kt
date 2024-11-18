package com.example.talevoice

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.talevoice.data.DefaultTaleRepository
import com.example.talevoice.data.TaleRepository
import com.example.talevoice.data.source.local.TaleDatabase
import com.example.talevoice.data.source.server.TaleApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaleApplication : Application() {

    private val appDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val appScope: CoroutineScope = CoroutineScope(SupervisorJob() + appDispatcher)

    private val database: TaleDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaleDatabase::class.java,
            "tale_database"
        ).build()
    }

    lateinit var taleRepository: TaleRepository
        private set

    override fun onCreate() {
        super.onCreate()
        Log.d("TaleApplication","tale application on create")

        appScope.launch(appDispatcher) {
            val apiService: TaleApiService = Retrofit.Builder()
                .baseUrl("http://20.41.121.42:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TaleApiService::class.java)

            // Repository 초기화
            taleRepository = DefaultTaleRepository(
                localDataSource = database.taleDao(),
                networkApiService = apiService,
                dispatcher = Dispatchers.IO
            )
        }

    }

}