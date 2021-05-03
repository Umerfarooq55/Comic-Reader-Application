package com.example.shortcut.ui.fragment.favourites.interactor

import androidx.paging.DataSource
import com.example.shortcut.interactor.BaseRxInteractor
import com.example.shortcut.repo.ComicsRepo
import com.example.shortcut.scheduler.ExecutionThreads
import com.example.shortcut.ui.model.UiComicStrip
import com.example.shortcut.ui.model.UiComicStripMapper

import io.reactivex.Single
import javax.inject.Inject

class LoadFavouritePagesInteractor @Inject constructor(
        private val comicsRepo: ComicsRepo,
        private val uiComicStripMapper: UiComicStripMapper,
        private val executionThreads: ExecutionThreads
) : BaseRxInteractor() {

    fun getPages(): DataSource.Factory<Int, UiComicStrip> {
        return Single.fromCallable {
            comicsRepo.getPagedFavourites()
        }
            .doOnSubscribe(this::addDisposable)
            .subscribeOn(executionThreads.jobExecutionThread)
            .map { pagedItems ->
                pagedItems.map { comicEntity ->
                    uiComicStripMapper.convert(comicEntity)
                }
            }
            .blockingGet()
    }
}
