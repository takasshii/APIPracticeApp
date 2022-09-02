package com.example.apipracticeapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.apipracticeapp.db.RepoDAO
import com.example.apipracticeapp.db.RepoDatabase
import com.example.apipracticeapp.db.RemoteKeys
import com.example.apipracticeapp.db.RemoteKeysDao
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator (
    private val service: ApiService,
    private val repoDatabase: RepoDatabase,
    private val repoDAO: RepoDAO,
    private val remoteKeysDAO: RemoteKeysDao
) : RemoteMediator<Int, Item>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Item>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val apiResponse = service.fetchRepositoryData(header = "application/vnd.github.v3+json", page, state.config.pageSize)

            val repos = convertToItem(apiResponse.body())

            val endOfPaginationReached = repos.isEmpty()
            repoDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDAO.clearRemoteKeys()
                    repoDAO.clearRepos()
                }
                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeysDAO.insertAll(keys)
                repoDAO.insertAll(repos)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Item>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Item>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Item>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                repoDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }

    // JsonGithubからList<Item>に変換する処理
    private fun convertToItem(data: JsonGithub?): List<Item> {
        // itemの格納
        val tempItems = mutableListOf<Item>()
        // Itemに変更
        data?.items?.forEach {
            tempItems.add(
                Item(
                    id = it.id,
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