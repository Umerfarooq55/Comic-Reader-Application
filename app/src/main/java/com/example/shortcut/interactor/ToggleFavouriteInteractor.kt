package com.example.shortcut.interactor

import com.example.shortcut.interactor.BaseRxInteractor
import com.example.shortcut.repo.ComicsRepo
import com.example.shortcut.scheduler.ExecutionThreads
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ToggleFavouriteInteractor @Inject constructor(
        private val comicsRepo: ComicsRepo,
        private val executionThreads: ExecutionThreads
) : BaseRxInteractor() {

    fun toggleFavourite(comicStripId: Int, isFavourite: Boolean) {
        addDisposable(Single.fromCallable {
            comicsRepo.getForComicStripId(comicStripId)
        }
            .doOnSubscribe(this::addDisposable)
            .subscribeOn(executionThreads.jobExecutionThread)
            .subscribe { optional ->
                if (optional.isPresent) {
                    Completable.fromAction {
                        comicsRepo.toggleFavourite(optional.get()!!, isFavourite)
                    }
                        .doOnSubscribe(this::addDisposable)
                        .subscribeOn(executionThreads.jobExecutionThread)
                        .subscribe()
                }
            }
        )
    }
}
