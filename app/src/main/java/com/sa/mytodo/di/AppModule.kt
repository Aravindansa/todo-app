package com.sa.mytodo.di

import android.app.Application
import android.content.Context
import com.sa.mytodo.data.data_sources.local.AppDatabase
import com.sa.mytodo.data.data_sources.remote.Api
import com.sa.mytodo.data.data_sources.remote.ResultCallAdapterFactory
import com.sa.mytodo.data.repository.TodoRepository
import com.sa.mytodo.domain.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApi (): Api {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client= OkHttpClient.Builder().apply {
            addInterceptor(logging)
        }.build()
        val retrofit= Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
        return retrofit.create(Api::class.java)
    }

    @Provides
    fun provideAppContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) =
        AppDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideDataRepository(api: Api, app: Application, db: AppDatabase):TodoRepository{
        return TodoRepository(app,api,db)
    }


}