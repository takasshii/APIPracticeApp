package com.example.apipracticeapp.di

import android.content.Context
import androidx.room.Room
import com.example.apipracticeapp.db.RepoDAO
import com.example.apipracticeapp.db.RepoDatabase
import com.example.apipracticeapp.db.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun provideRepoDatabase(
        @ApplicationContext context: Context
    ): RepoDatabase {
        return Room.databaseBuilder(
            context,
            RepoDatabase::class.java,
            "history.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepoDao(db: RepoDatabase): RepoDAO {
        return db.repoDAO()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(db: RepoDatabase): RemoteKeysDao {
        return db.remoteKeysDao()
    }
}
