package com.example.shortcut.interactor
import com.example.shortcut.interactor.BaseRxInteractor
import com.example.shortcut.repo.ComicsRepo
import com.example.shortcut.scheduler.ExecutionThreads

import io.reactivex.Completable
import javax.inject.Inject

class ResetCardPagesInteractor @Inject constructor(
        private val loader: ComicsRepo,
        private val executionThreads: ExecutionThreads
) : BaseRxInteractor() {

    fun resetData() {
        addDisposable(
            Completable.fromAction {
                loader.deleteCardPageData()
            }
                .subscribeOn(executionThreads.jobExecutionThread)
                .subscribe()
        )
    }
}
