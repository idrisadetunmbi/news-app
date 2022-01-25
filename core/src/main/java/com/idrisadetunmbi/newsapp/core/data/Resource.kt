package com.idrisadetunmbi.newsapp.core.data

import com.idrisadetunmbi.newsapp.core.data.Resource.Status.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

data class Resource<out T>(val status: Status, val data: T?, val exception: Throwable?) {
    operator fun invoke(): T? {
        return this.data
    }

    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = SUCCESS, data = data, exception = null)

        fun <T> error(data: T?, ex: Throwable): Resource<T> =
            Resource(status = ERROR, data = data, exception = ex)

        fun <T> loading(data: T?): Resource<T> = Resource(status = LOADING, data = data, exception = null)

        fun <T> initial(data: T? = null): Resource<T> = Resource(status = INITIAL, data = data, exception = null)
    }

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        INITIAL
    }
}

fun <T> Flow<T>.wrapAsResource(initialData: T? = null): Flow<Resource<T>> {
    return map { Resource.success(it) }
        .onStart { Resource.loading(initialData) }
        .catch { Resource.error(initialData, it) }
}
