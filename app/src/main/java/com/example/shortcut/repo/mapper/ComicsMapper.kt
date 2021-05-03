package com.example.shortcut.repo.mapper

import com.example.shortcut.data.db.entity.ComicEntity
import com.example.shortcut.net.api.model.ComicStrip


class ComicsMapper {

    fun convert(apiComics: List<ComicStrip>): List<ComicEntity> {

        val entities = ArrayList<ComicEntity>()
        apiComics.forEach {
            entities.add(convert(it))
        }
        return entities
    }

    fun convert(apiComic: ComicStrip): ComicEntity {

        return ComicEntity(
            num = apiComic.num,
            link = apiComic.link,
            news = apiComic.news,
            img = apiComic.img,
            title = apiComic.title,
            alt = apiComic.alt,
            safeTitle = apiComic.safeTitle,
            transcript = apiComic.transcript,
            day = apiComic.day,
            month = apiComic.month,
            year = apiComic.year
        )
    }
}
