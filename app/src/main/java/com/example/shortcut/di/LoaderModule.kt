package com.example.shortcut.di


import com.example.shortcut.RetrofitErrorMapper
import com.example.shortcut.data.db.dao.CardPagesDao
import com.example.shortcut.data.db.dao.ComicsDao
import com.example.shortcut.repo.ComicsRepo
import com.example.shortcut.repo.mapper.ComicsMapper

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class LoaderModule {

    @Provides
    internal fun providesComicsMapper(): ComicsMapper {
        return ComicsMapper()
    }

    @Provides
    internal fun providesLoader(
            retrofit: Retrofit,
            comicsDao: ComicsDao,
            cardPagesDao: CardPagesDao,
            comicMapper: ComicsMapper,
            retrofitErrorMapper: RetrofitErrorMapper
    ): ComicsRepo {
        return ComicsRepo(
            retrofit,
            comicsDao,
            cardPagesDao,
            comicMapper,
            retrofitErrorMapper
        )
    }
}
