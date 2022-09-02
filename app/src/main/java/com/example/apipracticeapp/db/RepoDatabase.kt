package com.example.apipracticeapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apipracticeapp.data.Item

@Database(
    version = 1,
    entities = [Item::class, RemoteKeys::class],
    exportSchema = false
)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun repoDAO(): RepoDAO
    abstract fun remoteKeysDao(): RemoteKeysDao
}