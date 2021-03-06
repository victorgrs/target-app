package com.target.android.network.providers

import com.target.android.BuildConfig
import com.target.android.network.services.AuthenticationInterceptor
import com.target.android.network.services.HeadersInterceptor
import com.target.android.network.services.ResponseInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ServiceProviderModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authenticationInterceptor: AuthenticationInterceptor,
        responseInterceptor: ResponseInterceptor
    ): OkHttpClient {
        val httpInterceptorLevel = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
                .addInterceptor(HeadersInterceptor())
                .addInterceptor(authenticationInterceptor)
                .addInterceptor(responseInterceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(httpInterceptorLevel))
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val url = URL_API ?: BuildConfig.API_URL
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(MoshiConverterFactory.create(moshi).withNullSerialization())
                .client(okHttpClient)
                .build()
    }

    companion object {
        var URL_API: String? = null
    }
}
