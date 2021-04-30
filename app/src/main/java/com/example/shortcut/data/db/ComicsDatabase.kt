package com.michaelfotiads.xkcdreader.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shortcut.data.db.dao.CardPagesDao
import com.example.shortcut.data.db.dao.ComicsDao
import com.example.shortcut.data.db.entity.CardPageEntity
import com.example.shortcut.data.db.entity.ComicEntity


@Database(
    entities = [ComicEntity::class, CardPageEntity::class],
    version = 1
)
abstract class ComicsDatabase : RoomDatabase() {

    abstract fun comicsDao(): ComicsDao

    abstract fun cardPagesDao(): CardPagesDao
}
