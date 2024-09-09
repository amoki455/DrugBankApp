package kishk.ahmedmohamed.drugbank.ui.fragments.drug

import dagger.hilt.android.lifecycle.HiltViewModel
import kishk.ahmedmohamed.drugbank.data.models.DrugListItem
import kishk.ahmedmohamed.drugbank.data.models.Response
import kishk.ahmedmohamed.drugbank.data.repositories.DrugRepository
import kishk.ahmedmohamed.drugbank.di.DrugsListPageLimit
import kishk.ahmedmohamed.drugbank.ui.base.BaseListViewModel
import javax.inject.Inject

@HiltViewModel
class DrugsListViewModel @Inject constructor(
    private val drugRepository: DrugRepository,
    @DrugsListPageLimit private val pageLimit: Long
) : BaseListViewModel<DrugListItem>() {
    override suspend fun requestData(page: Int): List<DrugListItem.Drug> {
        val startAfter = items.value?.lastOrNull()
        val response = if (startAfter == null) {
            drugRepository.getDrugs(pageLimit)
        } else {
            drugRepository.getDrugs(pageLimit, startAfter.drugId)
        }

        return when (response) {
            is Response.Success -> response.data
            is Response.Failure -> {
                response.error.printStackTrace()
                mError.postValue(response.error)
                emptyList()
            }
        }
    }
}