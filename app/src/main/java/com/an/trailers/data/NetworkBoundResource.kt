package com.an.trailers.data

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class NetworkBoundResource<ResultType, RequestType> @MainThread
protected constructor() {

    private val asObservable: Observable<Resource<ResultType>>

    init {
        val source: Observable<Resource<ResultType>>
        if (this.shouldFetch()) {

            source = this.createCall()
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    saveCallResult(processResponse(it)!!) }
                .doOnError { onFetchFailed() }
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    loadFromDb().toObservable()
                        .map { Resource.success(it) }
                }
//                .onErrorResumeNext { t : Throwable ->
//                    loadFromDb().toObservable().map {
//                        Resource.error(t.message!!, it)
//                    }
//                }

        } else {
            source = this.loadFromDb()
                .toObservable()
                .map { Resource.success(it) }
        }

        asObservable = Observable.concat(
            this.loadFromDb()
                .toObservable()
                .take(1)
                .map { Resource.loading(it) },
            source
        )
    }

    fun getAsObservable(): Observable<Resource<ResultType>> {
        return asObservable
    }

    private fun onFetchFailed() {}

    @WorkerThread
    protected fun processResponse(response: Resource<RequestType>): RequestType? {
        return response.data
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flowable<ResultType>

    @MainThread
    protected abstract fun createCall(): Observable<Resource<RequestType>>
}
