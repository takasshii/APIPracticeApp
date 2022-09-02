package com.example.apipracticeapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubPagingSource constructor(
    private val apiService: ApiService
) : PagingSource<Int, Item>() {
    // 新たにサーバーから取得するときに使う
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        // 現在のpositionを取得して、その1つ前か後を取得する
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    // メモリからデータを引き出すときに使う
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        // 場所を保持する変数 nullだったら1を入れる
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX

        return try {
            // ランキングを取得
            val response = apiService.fetchRepositoryData(
                header = "application/vnd.github.v3+json",
                position,
                params.loadSize
            )
            val repos = convertToItem(response.body())

            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    // JsonGithubからList<Item>に変換する処理
    private fun convertToItem(data: JsonGithub?): List<Item> {
        // itemの格納
        val tempItems = mutableListOf<Item>()
        // Itemに変更
        data?.items?.forEach {
            tempItems.add(
                Item(
                    name = it.name,
                    ownerIconUrl = it.owner.avatarUrl,
                    language = it.language,
                    stargazersCount = it.stargazersCount,
                    watchersCount = it.watchersCount,
                    forksCount = it.forksCount,
                    openIssuesCount = it.openIssuesCount
                )
            )
        }
        return tempItems
    }
}
