package kishk.ahmedmohamed.drugbank.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    @FireStoreDrugsCollection
    fun provideDrugsCollection() = "drugs"

    @Provides
    @Singleton
    @FireStoreDrugsInfoCollection
    fun provideDrugsInfoCollection() = "drugs_info"

    @Provides
    @Singleton
    @FireStoreDrugReferencesCollection
    fun provideDrugReferencesCollection() = "drug_references"

    @Provides
    @Singleton
    @FireStoreReferencesInfoCollection
    fun provideReferencesInfoCollection() = "references_info"

    @Provides
    @Singleton
    @DrugsListPageLimit
    fun provideDrugsListPageLimit() = 10L
}