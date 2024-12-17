package com.ismaelgr.data.di

import android.content.Context
import androidx.room.Room
import com.ismaelgr.data.Utils
import com.ismaelgr.data.repository.RepositoryImpl
import com.ismaelgr.data.retrofit.RetrofitDataSource
import com.ismaelgr.data.room.Database
import com.ismaelgr.data.room.RoomDataSource
import com.ismaelgr.domain.IRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // section Retrofit
    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return Retrofit.Builder()
            .baseUrl("https://www.loteriasyapuestas.es/servicios/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                Utils.getUnsafeOkHttpClient()
            )
            .build()
    }

    @Provides
    fun getRetrofitDataSource(retrofit: Retrofit): RetrofitDataSource =
        retrofit.create(RetrofitDataSource::class.java)

    // end section
    // section Room
    @Provides
    fun provideItemRepository(
        roomDataSource: RoomDataSource,
        retrofitDataSource: RetrofitDataSource
    ): IRepository = RepositoryImpl(roomDataSource, retrofitDataSource)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): Database = Room.databaseBuilder(context, Database::class.java, "MoneyTreeDatabase").build()

    @Singleton
    @Provides
    fun provideRoomDao(database: Database): RoomDataSource = database.dataSource()
    // end section
}