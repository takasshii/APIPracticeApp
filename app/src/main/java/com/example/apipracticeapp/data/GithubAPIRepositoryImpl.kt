package com.example.apipracticeapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.apipracticeapp.db.RepoDAO
import com.example.apipracticeapp.db.RepoDatabase
import com.example.apipracticeapp.db.RemoteKeysDao
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubAPIRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val repoDatabase: RepoDatabase,
    private val repoDAO: RepoDAO,
    private val remoteKeysDAO: RemoteKeysDao
) : GithubAPIRepository {
    override fun getRepository(): Flow<PagingData<Item>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = GithubRemoteMediator(
                service,
                repoDatabase,
                repoDAO,
                remoteKeysDAO
            ),
            pagingSourceFactory = { repoDAO.getAll() }
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