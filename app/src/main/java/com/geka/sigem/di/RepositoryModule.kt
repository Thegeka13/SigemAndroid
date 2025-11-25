package com.geka.sigem.di

import com.geka.sigem.data.repository.MarketplaceRepository
import com.geka.sigem.data.repository.MarketplaceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Vincula la Interfaz (MarketplaceRepository) a la Implementación (MarketplaceRepositoryImpl).
    @Binds
    @Singleton
    abstract fun bindMarketplaceRepository(
        // Hilt puede crear esta clase gracias a que le pusiste @Inject al constructor
        repositoryImpl: MarketplaceRepositoryImpl
    ): MarketplaceRepository
}