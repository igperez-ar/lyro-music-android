package com.lyro.music.core.capabilities.songs.infrastructure.factory

import com.lyro.music.core.capabilities.songs.infrastructure.datasource.MockSongsDataSource
import com.lyro.music.core.capabilities.songs.infrastructure.datasource.SongsDataSource
import com.lyro.music.core.capabilities.songs.repository.SongsRepository
import javax.inject.Inject
import javax.inject.Singleton

enum class DataSourceType {
    MOCK,       // For mock/fake data
    LOCAL,      // For device storage data (MediaStore)
    REMOTE,     // For future API implementation
    CACHE       // For future caching layer
}

@Singleton
class SongsFactory @Inject constructor(
    private val mockDataSource: MockSongsDataSource,
    private val localDataSource: SongsDataSource,
    private val dataSourceType: DataSourceType = DataSourceType.LOCAL // This doesn't work
) {
    fun create(): SongsRepository {
        return when (dataSourceType) {
            DataSourceType.MOCK -> localDataSource
            DataSourceType.LOCAL -> localDataSource
            DataSourceType.REMOTE -> throw NotImplementedError("Remote data source not implemented yet")
            DataSourceType.CACHE -> throw NotImplementedError("Cache data source not implemented yet")
        }
    }
} 