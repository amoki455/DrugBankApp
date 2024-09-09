package kishk.ahmedmohamed.drugbank.ui.fragments.drug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kishk.ahmedmohamed.drugbank.R
import kishk.ahmedmohamed.drugbank.data.models.DrugListItem
import kishk.ahmedmohamed.drugbank.databinding.FragmentDrugsListBinding
import kishk.ahmedmohamed.drugbank.ui.base.BaseListFragment

@AndroidEntryPoint
class DrugsListFragment : BaseListFragment<DrugListItem>(), MenuProvider {
    private var _binding: FragmentDrugsListBinding? = null
    private val binding get() = _binding!!
    override val viewModel: DrugsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().addMenuProvider(this)
        _binding = FragmentDrugsListBinding.inflate(inflater, container, false)
        binding.list.adapter = DrugRecyclerViewAdapter().apply {
            itemClickCallback = { itemOnClick(it) }
            reachedLastItemCallback = { viewModel.loadNextPage() }
        }
        observeViewModel()
        if (viewModel.items.value?.isEmpty() == true) {
            viewModel.loadNextPage()
        }
        return binding.root
    }

    override fun getLoadingIndicatorItem(): DrugListItem = DrugListItem.Loading
    override fun getEmptyListIndicatorItem(): DrugListItem = DrugListItem.NoContent

    override fun submitList(list: List<DrugListItem>) =
        (binding.list.adapter as DrugRecyclerViewAdapter).submitList(list)

    private fun itemOnClick(drug: DrugListItem.Drug) {
        val nav = findNavController()
        val action = DrugsListFragmentDirections.actionDrugsListFragmentToDrugInfoFragment(drug)
        runCatching {
            nav.navigate(action)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.drugs_list_fragment_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.menu_item_reload) {
            viewModel.reload()
            return true
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}