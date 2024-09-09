package kishk.ahmedmohamed.drugbank.ui.fragments.references

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kishk.ahmedmohamed.drugbank.data.models.DrugInfoReference
import kishk.ahmedmohamed.drugbank.data.models.InfoReferenceListItem
import kishk.ahmedmohamed.drugbank.data.models.Response
import kishk.ahmedmohamed.drugbank.data.repositories.DrugRepository
import kishk.ahmedmohamed.drugbank.ui.base.BaseViewModel

@HiltViewModel(assistedFactory = ReferencesListViewModelFactory::class)
class ReferencesListViewModel @AssistedInject constructor(
    private val drugRepository: DrugRepository,
    @Assisted val drugId: String
) : BaseViewModel() {

    private val mData = MutableLiveData(emptyList<InfoReferenceListItem>())
    val data: LiveData<List<InfoReferenceListItem>> = mData

    private val mNoContent = MutableLiveData(false)
    val noContent: LiveData<Boolean> = mNoContent

    var drugInfoReferences: List<DrugInfoReference> = emptyList()
        private set

    fun load() {
        if (drugId.isEmpty()) {
            mNoContent.value = true
            return
        }

        runOnScope {
            drugRepository.getDrugReferences(drugId).let {
                when (it) {
                    is Response.Failure -> {
                        mError.postValue(it.error)
                        mNoContent.value = true
                        return@runOnScope
                    }

                    is Response.Success -> drugInfoReferences = it.data
                }
            }

            if (drugInfoReferences.isEmpty()) {
                mNoContent.value = true
                return@runOnScope
            }

            val refIds = mutableListOf<String>()
            drugInfoReferences.forEach { item ->
                refIds.add(item.infoReferenceId)
            }

            drugRepository.getReferences(refIds).let { response ->
                when (response) {
                    is Response.Failure -> {
                        mError.value = response.error
                        mNoContent.value = true
                        return@runOnScope
                    }

                    is Response.Success -> mData.value = response.data.distinct().sortedBy { item ->
                        drugInfoReferences.find { it.infoReferenceId == item.referenceId }
                            ?.infoReferenceOrder
                    }
                }
            }
        }
    }
}