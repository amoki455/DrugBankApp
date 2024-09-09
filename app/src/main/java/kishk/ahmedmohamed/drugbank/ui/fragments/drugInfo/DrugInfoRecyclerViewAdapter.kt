package kishk.ahmedmohamed.drugbank.ui.fragments.drugInfo

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kishk.ahmedmohamed.drugbank.data.models.DrugInfoListItem
import kishk.ahmedmohamed.drugbank.databinding.DrugInfoItemBinding
import kishk.ahmedmohamed.drugbank.databinding.EmptyListIndicatorBinding
import kishk.ahmedmohamed.drugbank.databinding.LoadingItemBinding
import kishk.ahmedmohamed.drugbank.utils.SpannedText.restyleLinks
import kishk.ahmedmohamed.drugbank.utils.TextViewLinkHandler

class DrugInfoRecyclerViewAdapter :
    ListAdapter<DrugInfoListItem, DrugInfoRecyclerViewAdapter.ViewHolder>(diffCallback) {

    var linkClickCallback: ((link: String) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DrugInfoListItem.DrugInfo -> 1
            DrugInfoListItem.Loading -> 2
            DrugInfoListItem.NoContent -> 3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            1 -> DrugInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            is DrugInfoListItem.DrugInfo -> {
                with(holder.binding as DrugInfoItemBinding) {
                    infoTitle.text = item.name
                    details.movementMethod = linkHandler
                    details.text = Html.fromHtml(item.value, Html.FROM_HTML_MODE_COMPACT)
                        .restyleLinks()
                }
            }

            else -> Unit
        }
    }

    private val linkHandler = object : TextViewLinkHandler() {
        override fun onLinkClick(url: String?) {
            if (url != null)
                linkClickCallback?.invoke(url)
        }
    }

    inner class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<DrugInfoListItem>() {
            override fun areItemsTheSame(
                oldItem: DrugInfoListItem,
                newItem: DrugInfoListItem
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: DrugInfoListItem,
                newItem: DrugInfoListItem
            ): Boolean = oldItem == newItem
        }
    }
}