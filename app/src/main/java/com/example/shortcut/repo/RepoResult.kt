package com.example.shortcut.repo

import com.example.shortcut.repo.error.DataSourceError


class RepoResult<T>(
        val payload: T? = null,
        val dataSourceError: DataSourceError? = null,
        val next: String? = null
)
