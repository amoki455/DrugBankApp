package kishk.ahmedmohamed.drugbank.ui.fragments.drugInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kishk.ahmedmohamed.drugbank.data.models.DrugInfoListItem
import kishk.ahmedmohamed.drugbank.data.models.Response
import kishk.ahmedmohamed.drugbank.data.repositories.DrugRepository
import kishk.ahmedmohamed.drugbank.ui.base.BaseViewModel

@HiltViewModel(assistedFactory = DrugInfoViewModelFactory::class)
class DrugInfoViewModel @AssistedInject constructor(
    private val drugRepository: DrugRepository,
    @Assisted val drugId: String
) : BaseViewModel() {

    private val mData = MutableLiveData(listOf<DrugInfoListItem.DrugInfo>())
    val data: LiveData<List<DrugInfoListItem.DrugInfo>> = mData

    private val mNoContent = MutableLiveData(false)
    val noContent: LiveData<Boolean> = mNoContent

    fun load() {
        if (drugId.isEmpty()) {
            mNoContent.value = true
            return
        }

        runOnScope {
           when (val response = drugRepository.getDrugInfo(drugId)) {
                is Response.Success -> {
                    if (response.data == null) {
                        mNoContent.value = true
                        return@runOnScope
                    } else {
                        mData.value = response.data.toListItem()
                        mNoContent.value = mData.value?.isEmpty() == true
                    }
                }

                is Response.Failure -> {
                    mError.value = response.error
                    mNoContent.value = true
                    return@runOnScope
                }
            }
        }
    }
}