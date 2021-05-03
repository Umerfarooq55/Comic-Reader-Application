package com.example.shortcut.interactor
import com.example.shortcut.data.db.entity.ComicEntity
import com.example.shortcut.interactor.BaseRxInteractor
import com.example.shortcut.net.resolver.NetworkResolver
import com.example.shortcut.repo.ComicsRepo
import com.example.shortcut.repo.RepoResult
import com.example.shortcut.repo.error.DataSourceError
import com.example.shortcut.repo.error.DataSourceErrorKind
import com.example.shortcut.scheduler.ExecutionThreads
import com.example.shortcut.ui.error.UiError
import com.example.shortcut.ui.error.UiErrorMapper
import com.example.shortcut.ui.model.UiComicStrip
import com.example.shortcut.ui.model.UiComicStripMapper

import com.michaelfotiads.xkcdreader.data.prefs.UserDataStore

import io.reactivex.Single
import javax.inject.Inject

class LoadSpecificComicInteractor @Inject constructor(
        private val comicsRepo: ComicsRepo,
        private val dataStore: UserDataStore,
        private val networkResolver: NetworkResolver,
        private val uiComicStripMapper: UiComicStripMapper,
        private val uiErrorMapper: UiErrorMapper,
        private val executionThreads: ExecutionThreads
) : BaseRxInteractor() {

    interface Callback {
        fun onSuccess(uiComicStrip: UiComicStrip)
        fun onError(uiError: UiError)
    }

    fun load(comicStripId: Int, callback: Callback) {

        if (networkResolver.isConnected()) {
            val operation = if (comicStripId <= 0) {
                getLatestComicOperation(callback)
            } else {
                getSpecificComicOperation(comicStripId, callback)
            }
            addDisposable(operation
                .subscribeOn(executionThreads.jobExecutionThread)
                .subscribe()
            )
        } else {
            callback.onError(uiErrorMapper.convert(DataSourceError("", DataSourceErrorKind.NO_NETWORK)))
        }
    }

    private fun getLatestComicOperation(callback: Callback): Single<RepoResult<ComicEntity>> {
        return comicsRepo.loadLatestComic()
            .doOnSuccess { result ->
                when {
                    result.dataSourceError != null ->
                        callback.onError(uiErrorMapper.convert(result.dataSourceError))
                    result.payload != null -> {
                        val uiItem = uiComicStripMapper.convert(result.payload)
                        if (uiItem.number > dataStore.maxStripIndex) {
                            dataStore.maxStripIndex = uiItem.number
                        }
                        callback.onSuccess(uiItem)
                    }
                }
            }
    }

    private fun getSpecificComicOperation(comicStripId: Int, callback: Callback): Single<RepoResult<ComicEntity>> {
        return comicsRepo.loadComicWithId(comicStripId)
            .doOnSuccess { result ->
                when {
                    result.dataSourceError != null ->
                        callback.onError(uiErrorMapper.convert(result.dataSourceError))
                    result.payload != null -> {
                        val uiItem = uiComicStripMapper.convert(result.payload)
                        if (uiItem.number > dataStore.maxStripIndex) {
                            dataStore.maxStripIndex = uiItem.number
                        }
                        callback.onSuccess(uiItem)
                    }
                }
            }
    }
}
