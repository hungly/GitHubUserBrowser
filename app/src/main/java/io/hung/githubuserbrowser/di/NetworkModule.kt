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

import dagger.Module

@Module
class NetworkModule {

//    @Singleton
//    @Provides
//    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
//        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
//            override fun log(message: String) {
//                Timber.d(message)
//            }
//        }).apply {
//            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
//        }
//
//    @Named(AUTHORIZED_HEADERS_INTERCEPTOR)
//    @Singleton
//    @Provides
//    fun provideRefreshTokenInterceptor(
//        localDataSource: UserLocalDataSource,
//        @Named(STANDARD_HTTP_CLIENT) okHttpClient: OkHttpClient,
//        moshiConverterFactory: MoshiConverterFactory
//    ): Interceptor = object : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response = synchronized(this@NetworkModule) {
//            val token = localDataSource.getUserToken()
//            val originalRequest = chain.request()
//            val initialResponse = token?.let {
//                chain.proceed(
//                    originalRequest.newBuilder()
//                        .addHeader("Content-Type", "application/json")
//                        .addHeader("Authorization", "Bearer $it")
//                        .build()
//                )
//            } ?: chain.proceed(originalRequest)
//
//            when (initialResponse.code) {
//                401 -> {
//                    val refreshTokenResponse = runBlocking {
//                        localDataSource.getUserRefreshToken()?.let {
//                            provideApiService(okHttpClient, moshiConverterFactory, UserService::class.java)
//                                .refreshToken(RefreshTokenRequest(refreshToken = it)).execute()
//                        }
//                    }
//
//                    when (refreshTokenResponse?.isSuccessful) {
//                        false -> initialResponse
//                        else -> {
//                            initialResponse.close()
//                            val refreshToken = refreshTokenResponse?.body()
//                            refreshToken?.let {
//                                localDataSource.updateUserToken(it)?.let { account ->
//                                    localDataSource.saveUserAccount(account)
//                                }
//                                chain.proceed(
//                                    originalRequest.newBuilder()
//                                        .addHeader("Content-Type", "application/json")
//                                        .addHeader("Authorization", "Bearer ${it.token}")
//                                        .build()
//                                )
//                            } ?: initialResponse
//                        }
//                    }
//                }
//                else -> initialResponse
//            }
//        }
//    }
//
//    @Named(STANDARD_HEADERS_INTERCEPTOR)
//    @Singleton
//    @Provides
//    fun provideHttpHeaderInterceptor(): Interceptor = object : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val request = chain.request().newBuilder()
//                .addHeader("Content-Type", "application/json")
//                .build()
//            return chain.proceed(request)
//        }
//    }
//
//    @Singleton
//    @Provides
//    fun provideMoshi(): Moshi = Moshi.Builder()
//        .add(AccountJsonAdapter())
//        .add(RefreshTokenJsonAdapter())
//        .add(NewsJsonAdapter())
//        .add(ProgrammeHighlightJsonAdapter())
//        .add(PrayerRequestJsonAdapter())
//        .build()
//
//    @Singleton
//    @Provides
//    fun provideMoshiAccountAdapter(): JsonAdapter<Account> =
//        Moshi.Builder().add(AccountJsonAdapter()).build().adapter(Account::class.java)
//
//    @Singleton
//    @Provides
//    fun provideMoshiErrorResponseAdapter(): JsonAdapter<ErrorResponse> =
//        Moshi.Builder().build().adapter(ErrorResponse::class.java)
//
//    @Singleton
//    @Provides
//    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)
//
//    @Named(STANDARD_HTTP_CLIENT)
//    @Singleton
//    @Provides
//    fun provideOkHttpClient(
//        httpLoggingInterceptor: HttpLoggingInterceptor,
//        @Named(STANDARD_HEADERS_INTERCEPTOR) httpHeadersInterceptor: Interceptor
//    ): OkHttpClient =
//        OkHttpClient.Builder()
//            .addInterceptor(httpLoggingInterceptor)
//            .addInterceptor(httpHeadersInterceptor)
//            .connectTimeout(BuildConfig.API_CONNECTION_TIMEOUT_MIN.toLong(), TimeUnit.MINUTES)
//            .readTimeout(BuildConfig.API_READ_TIMEOUT_MIN.toLong(), TimeUnit.MINUTES)
//            .writeTimeout(BuildConfig.API_WRITE_TIMEOUT_MIN.toLong(), TimeUnit.MINUTES)
//            .build()
//
//    @Named(AUTHORIZED_HTTP_CLIENT)
//    @Singleton
//    @Provides
//    fun provideAuthorizedOkHttpClient(
//        httpLoggingInterceptor: HttpLoggingInterceptor,
//        @Named(AUTHORIZED_HEADERS_INTERCEPTOR) httpHeadersInterceptor: Interceptor
//    ): OkHttpClient {
//        val dispatcher = Dispatcher()
//        dispatcher.maxRequests = 1
//        return OkHttpClient.Builder()
//            .dispatcher(dispatcher)
//            .addInterceptor(httpLoggingInterceptor)
//            .addInterceptor(httpHeadersInterceptor)
//            .connectTimeout(BuildConfig.API_CONNECTION_TIMEOUT_MIN.toLong(), TimeUnit.MINUTES)
//            .readTimeout(BuildConfig.API_READ_TIMEOUT_MIN.toLong(), TimeUnit.MINUTES)
//            .writeTimeout(BuildConfig.API_WRITE_TIMEOUT_MIN.toLong(), TimeUnit.MINUTES)
//            .build()
//    }
//
//    @Named(STANDARD_USER_SERVICE)
//    @Singleton
//    @Provides
//    fun provideUserService(@Named(STANDARD_HTTP_CLIENT) okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): UserService =
//        provideApplicationService(okHttpClient, moshiConverterFactory, UserService::class.java)
//
//    @Named(AUTHORIZED_USER_SERVICE)
//    @Singleton
//    @Provides
//    fun provideAuthorizedUserService(@Named(AUTHORIZED_HTTP_CLIENT) okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): UserService =
//        provideApiService(okHttpClient, moshiConverterFactory, UserService::class.java)
//
//    @Named(AUTHORIZED_NEWS_SERVICE)
//    @Singleton
//    @Provides
//    fun provideNewsService(@Named(AUTHORIZED_HTTP_CLIENT) okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): NewsService =
//        provideApiService(okHttpClient, moshiConverterFactory, NewsService::class.java)
//
//    @Named(AUTHORIZED_PRAYER_REQUEST_SERVICE)
//    @Singleton
//    @Provides
//    fun providePrayerRequestService(@Named(AUTHORIZED_HTTP_CLIENT) okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): PrayerRequestService =
//        provideApiService(okHttpClient, moshiConverterFactory, PrayerRequestService::class.java)
//
//    private fun provideApplicationRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit =
//        Retrofit.Builder().baseUrl(BuildConfig.APPLICATION_URL)
//            .client(okHttpClient)
//            .addConverterFactory(moshiConverterFactory)
//            .build()
//
//    private fun provideApiRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit =
//        Retrofit.Builder().baseUrl(BuildConfig.API_URL)
//            .client(okHttpClient)
//            .addConverterFactory(moshiConverterFactory)
//            .build()
//
//    private fun <T> provideApplicationService(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory, clazz: Class<T>): T =
//        provideApplicationRetrofit(okHttpClient, moshiConverterFactory).create(clazz)
//
//    private fun <T> provideApiService(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory, clazz: Class<T>): T =
//        provideApiRetrofit(okHttpClient, moshiConverterFactoryrterFactory).create(clazz)
}