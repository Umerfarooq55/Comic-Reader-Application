package com.example.shortcut.repo.error

import com.example.shortcut.repo.error.DataSourceError

interface ErrorMapper<in T> {
    fun convert(error: T?): DataSourceError
}
