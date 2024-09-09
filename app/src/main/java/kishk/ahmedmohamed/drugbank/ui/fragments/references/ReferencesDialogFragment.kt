package kishk.ahmedmohamed.drugbank.ui.fragments.references

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kishk.ahmedmohamed.drugbank.R
import kishk.ahmedmohamed.drugbank.databinding.FragmentReferencesDialogBinding

@AndroidEntryPoint
class ReferencesDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentReferencesDialogBinding? = null
    private val args: ReferencesDialogFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReferencesDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.beginTransaction()
            .replace(
                R.id.list_frame,
                ReferencesListFragment.newInstance(args.drugId, args.highlightReferenceId)
            ).commit()
    }

    override fun onStart() {
        super.onStart()
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogTheme

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}