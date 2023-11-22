package com.isfandroid.whattowatch.core.di

import androidx.room.Room
import com.isfandroid.whattowatch.core.data.source.local.LocalDataSource
import com.isfandroid.whattowatch.core.data.source.local.room.AppDatabase
import com.isfandroid.whattowatch.core.data.source.remote.RemoteDataSource
import com.isfandroid.whattowatch.core.data.source.remote.network.ApiService
import com.isfandroid.whattowatch.core.domain.repository.IAppRepository
import com.isfandroid.whattowatch.core.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val databaseModule = module {
    factory { get<AppDatabase>().appDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, Constants.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<IAppRepository> {
        com.isfandroid.whattowatch.core.data.source.AppRepository(
            get(),
            get()
        )
    }
}