package com.example.shortcut.data.db.entity

import androidx.room.Embedded
import com.example.shortcut.data.db.entity.CardPageEntity
import com.example.shortcut.data.db.entity.ComicEntity

data class CardPageWithComic(
    @Embedded var pageEntity: CardPageEntity,
    @Embedded var comicEntity: ComicEntity
)
