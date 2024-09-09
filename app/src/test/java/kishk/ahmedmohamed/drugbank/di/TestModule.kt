package kishk.ahmedmohamed.drugbank.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kishk.ahmedmohamed.drugbank.data.repositories.DrugRepository
import kishk.ahmedmohamed.drugbank.data.repositories.FirestoreDrugRepository
import kishk.ahmedmohamed.drugbank.data.repositories.TestDrugRepository
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RepositoryModule::class])
abstract class TestModule {

    @Binds
    @Singleton
    abstract fun bindDrugsRepository(
        drugRepository: TestDrugRepository
    ): DrugRepository

}