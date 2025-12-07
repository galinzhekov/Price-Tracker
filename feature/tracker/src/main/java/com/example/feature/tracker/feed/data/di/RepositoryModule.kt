package com.example.feature.tracker.feed.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.feature.tracker.feed.domain.repository.PriceTrackerRepositoryImpl // Correct import for implementation
import com.example.feature.tracker.feed.domain.repository.PriceTrackerRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPriceTrackerRepository(
        impl: PriceTrackerRepositoryImpl
    ): PriceTrackerRepository
}