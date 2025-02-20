package com.lyro.music.core.capabilities.songs.di

import com.lyro.music.core.capabilities.songs.infrastructure.factory.DataSourceType
import com.lyro.music.core.capabilities.songs.infrastructure.factory.SongsFactory
import com.lyro.music.core.capabilities.songs.repository.SongsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SongsModule {
    
    @Provides
    @Singleton
    fun provideSongsRepository(
        factory: SongsFactory
    ): SongsRepository = factory.create()

    @Provides
    @Singleton
    fun provideDataSourceType(): DataSourceType {
        return DataSourceType.MOCK
    }
}