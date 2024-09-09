package kishk.ahmedmohamed.drugbank.ui.base

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseListFragment<Item> : Fragment() {
    protected abstract val viewModel: BaseListViewModel<Item>
    protected abstract fun submitList(list: List<Item>)
    protected abstract fun getLoadingIndicatorItem(): Item
    protected abstract fun getEmptyListIndicatorItem(): Item

    protected fun observeViewModel() {
        viewModel.items.observe(viewLifecycleOwner, this::onReceivedNewItemsList)
        viewModel.isLoading.observe(viewLifecycleOwner, this::onLoading)
        viewModel.loadedAllData.observe(viewLifecycleOwner, this::onLoadedAllData)
        viewModel.error.observe(viewLifecycleOwner, this::onError)
    }

    private fun getCurrentList(): List<Item> = viewModel.items.value ?: emptyList()

    private fun onLoading(state: Boolean) {
        if (state) {
            submitList(getCurrentList() + getLoadingIndicatorItem())
        } else {
            submitList(getCurrentList())
        }
    }

    private fun onReceivedNewItemsList(newList: List<Item>) {
        submitList(newList)
    }

    private fun onLoadedAllData(state: Boolean) {
        val noItems = getCurrentList().isEmpty()
        if (state && noItems) {
            submitList(listOf(getEmptyListIndicatorItem()))
        }
    }

    private fun onError(error: Throwable?) {
        if (error == null)
            return

        view?.let {
            Snackbar.make(it, error.localizedMessage as CharSequence, Snackbar.LENGTH_LONG).show()
        }

        viewModel.clearErrorState()
    }
}