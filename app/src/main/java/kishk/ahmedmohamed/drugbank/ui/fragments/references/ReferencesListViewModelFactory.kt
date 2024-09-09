package kishk.ahmedmohamed.drugbank.ui.fragments.references

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ReferencesListViewModelFactory {
    fun create(drugId: String): ReferencesListViewModel
}