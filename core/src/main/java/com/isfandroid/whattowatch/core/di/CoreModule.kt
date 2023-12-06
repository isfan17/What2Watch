package com.isfandroid.whattowatch.core.di

import androidx.room.Room
import com.isfandroid.whattowatch.core.data.source.AppRepository
import com.isfandroid.whattowatch.core.data.source.local.LocalDataSource
import com.isfandroid.whattowatch.core.data.source.local.room.AppDatabase
import com.isfandroid.whattowatch.core.data.source.remote.RemoteDataSource
import com.isfandroid.whattowatch.core.data.source.remote.network.ApiService
import com.isfandroid.whattowatch.core.domain.repository.IAppRepository
import com.isfandroid.whattowatch.core.utils.Constants
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val hostName = Constants.API_HOSTNAME
        val certificatePinner = CertificatePinner.Builder()
            .add(hostName, "sha256/5VLcahb6x4EvvFrCF2TePZulWqrLHS2jCg9Ywv6JHog=")
            .add(hostName, "sha256/vxRon/El5KuI4vx5ey1DgmsYmRY0nDd5Cg4GfJ8S+bg=")
            .add(hostName, "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
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
        val passPhrase: ByteArray = SQLiteDatabase.getBytes(Constants.DB_PASS_PHRASE.toCharArray())
        val factory = SupportFactory(passPhrase)
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, Constants.DB_NAME
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<IAppRepository> {
        AppRepository(
            get(),
            get()
        )
    }
}