package kishk.ahmedmohamed.drugbank.ui.fragments.drugInfo

import dagger.assisted.AssistedFactory

@AssistedFactory
interface DrugInfoViewModelFactory {
    fun create(drugId: String): DrugInfoViewModel
}