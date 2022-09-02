package com.example.apipracticeapp.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.apipracticeapp.data.Item

@Dao
interface RepoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Item>)

    @Query("SELECT * FROM repos")
    fun getAll(): PagingSource<Int, Item>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}
