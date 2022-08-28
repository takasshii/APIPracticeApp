package com.example.apipracticeapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubAPIRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : GithubAPIRepository {
    override suspend fun getRepository(
        header: String,
        inputText: String
    ): APIResult<JsonGithub?> =
        withContext(Dispatchers.IO) {
            try {
                // 成功時
                val apiResult =
                    apiService.fetchRepositoryData(header, inputText)
                // WeatherAPIResultに変形
                APIResult.Success(
                    data = apiResult.body()
                )
            } catch (e: Exception) {
                // 失敗時
                APIResult.Error(
                    exception = e
                )
            }
        }
}

interface ApiService {
    @GET("search/repositories")
    suspend fun fetchRepositoryData(
        @Header("Accept") header: String,
        @Query("q") inputText: String
    ): Response<JsonGithub>
}