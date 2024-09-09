package kishk.ahmedmohamed.drugbank.ui.fragments.drugInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kishk.ahmedmohamed.drugbank.R
import kishk.ahmedmohamed.drugbank.data.models.DrugInfoListItem
import kishk.ahmedmohamed.drugbank.databinding.FragmentDrugInfoBinding
import kishk.ahmedmohamed.drugbank.utils.ReferenceLink

@AndroidEntryPoint
class DrugInfoFragment : Fragment(), MenuProvider {
    private var _binding: FragmentDrugInfoBinding? = null
    private val binding get() = _binding!!
    private val args: DrugInfoFragmentArgs by navArgs()
    private val viewModel: DrugInfoViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<DrugInfoViewModelFactory> {
                it.create(args.drug.drugId)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
        _binding = FragmentDrugInfoBinding.inflate(inflater, container, false)
        binding.list.adapter = DrugInfoRecyclerViewAdapter().apply {
            linkClickCallback = { onReferenceLinkClick(it) }
        }
        observeViewModel()
        if (viewModel.data.value?.isEmpty() == true) {
            (binding.list.adapter as DrugInfoRecyclerViewAdapter)
                .submitList(listOf(DrugInfoListItem.Loading))
            viewModel.load()
        }
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.noContent.observe(viewLifecycleOwner) {
            if (it) {
                (binding.list.adapter as DrugInfoRecyclerViewAdapter)
                    .submitList(listOf(DrugInfoListItem.NoContent))
            } else if (viewModel.data.value?.isNotEmpty() == true) {
                (binding.list.adapter as DrugInfoRecyclerViewAdapter)
                    .submitList(viewModel.data.value)
            }
        }

        viewModel.data.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                (binding.list.adapter as DrugInfoRecyclerViewAdapter).submitList(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner, this::onError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.drug.name
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.drug_info_fragment_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.menu_item_references) {
            navigateToReferences()
            return true
        }
        return false
    }

    private fun onReferenceLinkClick(link: String) {
        ReferenceLink.getReferenceId(link)?.let { referenceId ->
            navigateToReferences(referenceId)
        }
    }

    private fun navigateToReferences(highlightReferenceId: String? = null) {
        val nav = findNavController()
        val action = DrugInfoFragmentDirections
            .actionDrugInfoFragmentToReferencesListDialogFragment(
                args.drug.drugId,
                highlightReferenceId
            )
        runCatching {
            nav.navigate(action)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}