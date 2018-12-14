package com.an.trailers.data

import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class NetworkBoundResource<ResultType, RequestType> @MainThread
protected constructor() {

    private val asObservable: Observable<Resource<ResultType>>

    init {
        val source: Observable<Resource<ResultType>>
        if (shouldFetch()) {

            source = createCall()
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    saveCallResult(processResponse(it)!!) }

                .flatMap {
                    loadFromDb().toObservable()
                        .map { Resource.success(it) } }

                .doOnError { onFetchFailed() }

                .onErrorResumeNext { t : Throwable ->
                    loadFromDb().toObservable().map {
                        Resource.error(t.message!!, it) }
                }

                .observeOn(AndroidSchedulers.mainThread())

        } else {
            source = loadFromDb()
                .toObservable()
                .map { Resource.success(it) }
        }

        asObservable = Observable.concat(
            loadFromDb()
                .toObservable()
                .map { Resource.loading(it) }
                .take(1),
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