package kishk.ahmedmohamed.drugbank.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kishk.ahmedmohamed.drugbank.data.repositories.DrugRepository
import kishk.ahmedmohamed.drugbank.data.repositories.FirestoreDrugRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDrugsRepository(
        drugRepository: FirestoreDrugRepository
    ): DrugRepository

}