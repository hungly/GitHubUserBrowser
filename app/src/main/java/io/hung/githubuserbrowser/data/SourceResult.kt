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

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

data class SourceResult<out T>(
    val status: Status,
    val data: T?,
    val errorMessage: String? = null
) {

    enum class Status { INITIALIZED, LOADING, SUCCESS, ERROR }

    companion object {

        fun <T> initialize() = SourceResult<T>(Status.INITIALIZED, null, null)

        fun <T> loading(data: T? = null) = SourceResult<T>(Status.LOADING, data, null)

        fun <T> success(data: T?) = SourceResult<T>(Status.SUCCESS, data, null)

        fun <T> error(data: T? = null, errorMessage: String?) = SourceResult<T>(Status.ERROR, data, errorMessage)
    }
}

fun <T> getResultIntoLiveData(
    databaseQuery: () -> T?,
    networkCall: suspend () -> SourceResult<T?>,
    saveResult: suspend (T?) -> Unit,
    resultLiveData: MutableLiveData<SourceResult<T?>>,
    waitingDuration: Long = 0,
    scope: CoroutineScope
) {
    scope.launch(Dispatchers.IO) {
        try {
            resultLiveData.postValue(SourceResult.loading())
            resultLiveData.postValue(SourceResult.success(databaseQuery()))

            val networkResponse = networkCall()

            if (waitingDuration > 0) delay(waitingDuration)

            if (networkResponse.status == SourceResult.Status.SUCCESS) {
                saveResult(networkResponse.data)
                resultLiveData.postValue(networkResponse)
            } else if (networkResponse.status == SourceResult.Status.ERROR) {
                resultLiveData.postValue(SourceResult.error(errorMessage = networkResponse.errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}