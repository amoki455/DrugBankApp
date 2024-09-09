package kishk.ahmedmohamed.drugbank.ui.fragments.drug

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kishk.ahmedmohamed.drugbank.data.models.DrugListItem
import kishk.ahmedmohamed.drugbank.databinding.DrugItemBinding
import kishk.ahmedmohamed.drugbank.databinding.EmptyListIndicatorBinding
import kishk.ahmedmohamed.drugbank.databinding.LoadingItemBinding

class DrugRecyclerViewAdapter :
    ListAdapter<DrugListItem, DrugRecyclerViewAdapter.ViewHolder>(diffCallback) {

    var itemClickCallback: ((DrugListItem.Drug) -> Unit)? = null
    var reachedLastItemCallback: (() -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DrugListItem.Drug -> 1
            DrugListItem.Loading -> 2
            DrugListItem.NoContent -> 3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            1 -> DrugItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            is DrugListItem.Drug -> {
                with(holder.binding as DrugItemBinding) {
                    drugName.text = item.name
                    summary.text = item.summary

                    root.setOnClickListener {
                        itemClickCallback?.invoke(item)
                    }

                }
            }

            else -> Unit
        }

        if (position == itemCount - 1) {
            reachedLastItemCallback?.invoke()
        }
    }

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<DrugListItem>() {
            override fun areItemsTheSame(oldItem: DrugListItem, newItem: DrugListItem): Boolean =
                oldItem.drugId == newItem.drugId

            override fun areContentsTheSame(oldItem: DrugListItem, newItem: DrugListItem): Boolean =
                oldItem == newItem
        }
    }
}