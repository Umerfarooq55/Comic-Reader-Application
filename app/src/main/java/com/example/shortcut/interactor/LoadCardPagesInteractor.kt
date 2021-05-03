package com.example.shortcut.interactor

import androidx.paging.DataSource
import com.example.shortcut.interactor.BaseRxInteractor
import com.example.shortcut.repo.ComicsRepo
import com.example.shortcut.scheduler.ExecutionThreads
import com.example.shortcut.ui.model.UiComicStrip
import com.example.shortcut.ui.model.UiComicStripMapper

import io.reactivex.Single
import javax.inject.Inject

class LoadCardPagesInteractor @Inject constructor(
        private val comicsRepo: ComicsRepo,
        private val uiComicStripMapper: UiComicStripMapper,
        private val executionThreads: ExecutionThreads
) : BaseRxInteractor() {

    fun getPages(): DataSource.Factory<Int, UiComicStrip> {
        return Single.fromCallable {
            comicsRepo.getCardPagedComics()
        }
            .doOnSubscribe(this::addDisposable)
            .subscribeOn(executionThreads.jobExecutionThread)
            .map { pagedItems ->
                pagedItems.map(uiComicStripMapper::convert)
            }
            .blockingGet()
    }
}
