/*
 * MIT License
 *
 * Copyright (c) 2020 Hung Quoc Ly
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package io.hung.githubuserbrowser.di

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.hung.githubuserbrowser.BuildConfig
import io.hung.githubuserbrowser.api.model.ApiError
import io.hung.githubuserbrowser.api.service.UserService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.d(message)
            }
        }).apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    @Singleton
    @Provides
    fun provideHttpHeaderInterceptor(@Named(ACCESS_TOKEN) accessToken: String): Interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "token $accessToken")
                .build()
            return chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .build()

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideMoshiErrorResponseAdapter(): JsonAdapter<ApiError> =
        Moshi.Builder().build().adapter(ApiError::class.java)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        httpHeadersInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(httpHeadersInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()

    @Singleton
    @Provides
    fun provideUserService(@Named(API_URL) apiUrl: String, okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): UserService =
        provideApiService(apiUrl, okHttpClient, moshiConverterFactory, UserService::class.java)


    private fun provideApiRetrofit(apiUrl: String, okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit =
        Retrofit.Builder().baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()

    private fun <T> provideApiService(apiUrl: String, okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory, clazz: Class<T>): T =
        provideApiRetrofit(apiUrl, okHttpClient, moshiConverterFactory).create(clazz)
}