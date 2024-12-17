package com.ismaelgr.presentation.di

import com.ismaelgr.presentation.navigation.DefaultNavigator
import com.ismaelgr.presentation.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
data object PresentationModule {

    @Provides
    @Singleton
    fun provideNavigator(): Navigator = DefaultNavigator()
}