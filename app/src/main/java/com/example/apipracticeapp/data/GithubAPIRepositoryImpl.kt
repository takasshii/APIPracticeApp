package com.example.apipracticeapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubAPIRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : GithubAPIRepository {
    override fun getRepository(): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource(apiService) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}

interface ApiService {
    @GET("search/repositories?q=stars:>1")
    suspend fun fetchRepositoryData(
        @Header("Accept") header: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Response<JsonGithub>
}