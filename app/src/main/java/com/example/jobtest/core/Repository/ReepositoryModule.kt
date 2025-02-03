package com.example.jobtest.core.Repository




import com.example.jobtest.core.network.RetrofitInstance
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(impl: RepositoryImpl): Repository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


}
