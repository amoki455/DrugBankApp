package kishk.ahmedmohamed.drugbank.ui.fragments.references

import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kishk.ahmedmohamed.drugbank.data.models.InfoReferenceListItem
import kishk.ahmedmohamed.drugbank.databinding.EmptyListIndicatorBinding
import kishk.ahmedmohamed.drugbank.databinding.LoadingItemBinding
import kishk.ahmedmohamed.drugbank.databinding.ReferenceItemBinding

class ReferencesRecyclerViewAdapter(
    val createSpannedText: (InfoReferenceListItem.InfoReference) -> Spanned
) :
    ListAdapter<InfoReferenceListItem, ReferencesRecyclerViewAdapter.ViewHolder>(
        diffCallback
    ) {

    var currentListChangedCallback: ((List<InfoReferenceListItem>) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is InfoReferenceListItem.InfoReference -> 1
            InfoReferenceListItem.Loading -> 2
            InfoReferenceListItem.NoContent -> 3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            1 -> ReferenceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            2 -> LoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> EmptyListIndicatorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is InfoReferenceListItem.InfoReference -> {
                with(holder.binding as ReferenceItemBinding) {
                    details.movementMethod = LinkMovementMethod()
                    details.text = createSpannedText(item)
                }
            }

            else -> Unit
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<InfoReferenceListItem>,
        currentList: MutableList<InfoReferenceListItem>
    ) {
        currentListChangedCallback?.invoke(currentList)
    }

    inner class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<InfoReferenceListItem>() {
            override fun areItemsTheSame(
                oldItem: InfoReferenceListItem,
                newItem: InfoReferenceListItem
            ): Boolean = oldItem.referenceId == newItem.referenceId

            override fun areContentsTheSame(
                oldItem: InfoReferenceListItem,
                newItem: InfoReferenceListItem
            ): Boolean = oldItem == newItem
        }
    }
}