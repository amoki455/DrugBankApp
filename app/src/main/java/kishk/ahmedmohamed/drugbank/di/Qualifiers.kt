package kishk.ahmedmohamed.drugbank.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FireStoreDrugsCollection


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FireStoreDrugsInfoCollection


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FireStoreDrugReferencesCollection


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FireStoreReferencesInfoCollection


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DrugsListPageLimit