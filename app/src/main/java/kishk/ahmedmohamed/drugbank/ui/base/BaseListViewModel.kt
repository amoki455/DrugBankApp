package kishk.ahmedmohamed.drugbank.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job

abstract class BaseListViewModel<TEntity> : BaseViewModel() {
    private var currentPage = 0
    private var nextPageLoadingJob: Job? = null
    private var isReloading = false

    private val mItems = MutableLiveData<List<TEntity>>(emptyList())
    val items: LiveData<List<TEntity>> = mItems

    private val mIsLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = mIsLoading

    private val mLoadedAllData = MutableLiveData(false)
    val loadedAllData: LiveData<Boolean> = mLoadedAllData

    protected abstract suspend fun requestData(page: Int): List<TEntity>

    fun loadNextPage() {
        if (nextPageLoadingJob?.isActive == true || mIsLoading.value == true || mLoadedAllData.value == true) {
            return
        }

        nextPageLoadingJob = runOnScope(
            onFailure = {
                mIsLoading.value = false
            }
        ) {
            mIsLoading.value = true
            val newItems = requestData(currentPage + 1)

            val currentItems = mItems.value ?: emptyList()
            mItems.value = currentItems + newItems

            mIsLoading.value = false

            if (newItems.isNotEmpty()) {
                currentPage += 1
            } else {
                mLoadedAllData.value = true
            }
        }
    }

    fun reload() {
        if (isReloading)
            return

        runOnScope(
            onFailure = {
                isReloading = false
            }
        ) {
            nextPageLoadingJob?.let {
                if (it.isActive) {
                    it.cancel()
                    it.join()
                }
            }
            clearItemsList()
            currentPage = 0
            mLoadedAllData.value = false
            loadNextPage()
            isReloading = false
        }
    }

    private fun clearItemsList() {
        val count = mItems.value?.size ?: 0
        if (count > 0) {
            mItems.value = emptyList()
        }
    }
}