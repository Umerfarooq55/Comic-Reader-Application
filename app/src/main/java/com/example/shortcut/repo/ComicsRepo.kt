package com.example.shortcut.repo

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.shortcut.RetrofitErrorMapper
import com.example.shortcut.data.db.dao.CardPagesDao
import com.example.shortcut.data.db.dao.ComicsDao
import com.example.shortcut.data.db.entity.CardPageEntity
import com.example.shortcut.data.db.entity.ComicEntity
import com.example.shortcut.net.api.ComicApi
import com.example.shortcut.net.api.model.ComicStrip
import com.example.shortcut.repo.mapper.ComicsMapper

import io.reactivex.Single
import java8.util.Optional
import retrofit2.Retrofit

class ComicsRepo(
        retrofit: Retrofit,
        private val comicsDao: ComicsDao,
        private val cardPagesDao: CardPagesDao,
        private val comicsMapper: ComicsMapper,
        private val errorMapper: RetrofitErrorMapper
) {

    private val comicApi = ComicApi(retrofit)

    fun loadLatestComic(): Single<RepoResult<ComicEntity>> {
        return comicApi.getLatest()
            .map { comicStrip ->
                val entity = upsertComicStripFromNet(comicStrip)
                RepoResult(entity)
            }
            .onErrorReturn { throwable ->
                RepoResult(dataSourceError = errorMapper.convert(throwable))
            }
    }

    fun loadComicWithId(comicId: Int): Single<RepoResult<ComicEntity>> {

        return Single.fromCallable {
            Optional.ofNullable(comicsDao.getForId(comicId))
        }
            .flatMap { optional ->
                if (optional.isPresent) {
                    val entity = optional.get()!!
                    cardPagesDao.upsert(CardPageEntity(entity.num))
                    Single.just(RepoResult(entity))
                } else {
                    comicApi.getForId(comicId = comicId.toString())
                        .map { comicStrip ->
                            val entity = upsertComicStripFromNet(comicStrip)
                            RepoResult(entity)
                        }
                        .onErrorReturn { throwable ->
                            RepoResult(dataSourceError = errorMapper.convert(throwable))
                        }
                }
            }
    }

    fun toggleFavourite(comicEntity: ComicEntity, isFavourite: Boolean) {
        comicEntity.isFavourite = isFavourite
        comicsDao.upsert(comicEntity)
    }

    fun getFavourites(): LiveData<List<ComicEntity>> {
        return comicsDao.getAllFavouritesLiveData()
    }

    fun getForComicStripId(comicStripId: Int): Optional<ComicEntity?> {
        return Optional.ofNullable(comicsDao.getForId(comicStripId))
    }

    fun getCardPagedComics(): DataSource.Factory<Int, ComicEntity> {
        return cardPagesDao.getComicsPaged().map { pageWithComic ->
            pageWithComic.comicEntity
        }
    }

    fun getComicPagedComics(): DataSource.Factory<Int, ComicEntity> {
        return comicsDao.getComicsPaged()
    }

    fun getPagedFavourites(): DataSource.Factory<Int, ComicEntity> {
        return comicsDao.getFavouritesPaged()
    }

    fun deleteCardPageData() {
        cardPagesDao.deleteAll()
    }

    private fun upsertComicStripFromNet(comicStrip: ComicStrip): ComicEntity {
        val entity = comicsMapper.convert(comicStrip)
        comicsDao.getForId(comicStrip.num)?.let { existingEntity ->
            entity.isFavourite = existingEntity.isFavourite
        }
        comicsDao.upsert(entity)
        cardPagesDao.upsert(CardPageEntity(entity.num))
        return entity
    }
}
