package com.example.apipracticeapp.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GithubAPIRepository {
    // APIResult型を返す
    fun getRepository(): Flow<PagingData<Item>>
}

