package com.raj.newsapp.di

import com.raj.newsapp.model.repository.NewsRepository
import com.raj.newsapp.model.repository.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@InstallIn(ViewModelComponent::class)
@Module
interface NewsModule {

    @Binds //Note:
    abstract fun providerNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository
}