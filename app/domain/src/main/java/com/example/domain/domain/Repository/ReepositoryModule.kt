package com.example.domain.domain.Repository

import com.example.domain.domain.usecase.GetOfferUsecase
import com.example.domain.domain.usecase.GetVacanciesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(impl: RepositoryImpl): Repository {
        return impl
    }

    @Provides
    @Singleton
    fun provideGetOfferUsecase(repository: Repository): GetOfferUsecase {
        return GetOfferUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideGetVacanciesUseCase(repository: Repository): GetVacanciesUseCase {
        return GetVacanciesUseCase(repository)
    }
}
