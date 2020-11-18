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

package io.hung.githubuserbrowser.data

import com.squareup.moshi.JsonAdapter
import io.hung.githubuserbrowser.api.model.ApiError
import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource(private val adapter: JsonAdapter<ApiError>) {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): SourceResult<T> =
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                SourceResult.success(body)
            } else {
                val error = response.errorBody()?.let { adapter.fromJson(it.string()) }
                error(response.code(), error?.message ?: response.errorBody()?.string() ?: response.message())
            }
        } catch (e: Exception) {
            error(e.message ?: e.toString())
        }

    private fun <T> error(statusCode: Int?, errorMessage: String): SourceResult<T> {
        Timber.e("Network call has failed for a following reason: $statusCode $errorMessage")
        return SourceResult.error(errorMessage = errorMessage)
    }
}