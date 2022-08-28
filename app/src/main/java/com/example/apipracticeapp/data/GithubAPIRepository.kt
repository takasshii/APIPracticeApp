package com.example.apipracticeapp.data

// 結果を返すクラス
sealed class APIResult<out R> {
    // 成功した場合
    data class Success<out T>(val data: T) : APIResult<T>()

    // 失敗した場合
    data class Error(val exception: Throwable) : APIResult<Nothing>()
}

interface GithubAPIRepository {
    // APIResult型を返す
    suspend fun getRepository(
        header: String,
        inputText: String
    ): APIResult<JsonGithub?>
}
