package com.example.pricetracker.di

import com.example.core.navigation.AppNavigator
import com.example.core.navigation.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppNavigator(
        impl: AppNavigatorImpl
    ): AppNavigator
}