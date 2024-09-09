package kishk.ahmedmohamed.drugbank.ui.fragments.references

import android.os.Bundle
import android.text.Spanned
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kishk.ahmedmohamed.drugbank.R
import kishk.ahmedmohamed.drugbank.data.models.InfoReferenceListItem
import kishk.ahmedmohamed.drugbank.databinding.FragmentReferencesListBinding
import kishk.ahmedmohamed.drugbank.utils.SpannedText

@AndroidEntryPoint
class ReferencesListFragment : Fragment() {
    private var _binding: FragmentReferencesListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReferencesListViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<ReferencesListViewModelFactory> {
                it.create(arguments?.getString(ARG_DRUG_ID) ?: "")
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReferencesListBinding.inflate(inflater, container, false)
        binding.list.adapter = ReferencesRecyclerViewAdapter(this::createItemSpannedText).apply {
            currentListChangedCallback = { onCurrentListChanged(it) }
        }
        observeViewModel()
        if (viewModel.data.value?.isEmpty() == true) {
            (binding.list.adapter as ReferencesRecyclerViewAdapter)
                .submitList(listOf(InfoReferenceListItem.Loading))
            viewModel.load()
        }
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.noContent.observe(viewLifecycleOwner) {
            if (it) {
                (binding.list.adapter as ReferencesRecyclerViewAdapter)
                    .submitList(listOf(InfoReferenceListItem.NoContent))
            } else if (viewModel.data.value?.isNotEmpty() == true) {
                (binding.list.adapter as ReferencesRecyclerViewAdapter)
                    .submitList(viewModel.data.value)
            }
        }

        viewModel.data.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                (binding.list.adapter as ReferencesRecyclerViewAdapter).submitList(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner, this::onError)
    }


    private fun createItemSpannedText(reference: InfoReferenceListItem.InfoReference): Spanned {
        val drugInfoRef = viewModel.drugInfoReferences
            .find { it.infoReferenceId == reference.referenceId } ?: return SpannedString("")
        val highlightColor = if (
            arguments?.getString(ARG_HIGHLIGHTED_REF_ID) == reference.referenceId
        ) {
            requireContext().getColor(R.color.transparent_yellow)
        } else null
        return SpannedText.createReferenceSpannedText(
            reference,
            drugInfoRef,
            MaterialColors.getColor(requireView(), R.attr.referenceOrderColor),
            highlightColor
        )
    }

    private fun animateBottomSheetDialog() {
        (parentFragment as? BottomSheetDialogFragment)?.dialog
            ?.window?.decorView?.let {
                val viewGroup = (it as? ViewGroup)
                if (viewGroup != null)
                    TransitionManager.beginDelayedTransition(viewGroup)
            }
    }

    private fun scrollToHighlightedItem(list: List<InfoReferenceListItem>) {
        val highlightedReferenceId = arguments?.getString(ARG_HIGHLIGHTED_REF_ID)
        if (highlightedReferenceId?.isEmpty() == false) {
            val pos = list.indexOfFirst { it.referenceId == highlightedReferenceId }
            (binding.list.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                pos,
                0
            )
        }
    }

    private fun onCurrentListChanged(list: List<InfoReferenceListItem>) {
        animateBottomSheetDialog()
        scrollToHighlightedItem(list)
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

    companion object {
        const val ARG_DRUG_ID = "drug_id"
        const val ARG_HIGHLIGHTED_REF_ID = "highlight_reference_id"

        fun newInstance(drugId: String, highlightedReferenceId: String?): ReferencesListFragment {
            return ReferencesListFragment().apply {
                arguments = bundleOf(
                    ARG_DRUG_ID to drugId,
                    ARG_HIGHLIGHTED_REF_ID to highlightedReferenceId
                )
            }
        }
    }
}