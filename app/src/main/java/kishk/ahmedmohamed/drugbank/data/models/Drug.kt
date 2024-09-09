package kishk.ahmedmohamed.drugbank.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

sealed interface DrugListItem {
    val drugId: String

    data object Loading : DrugListItem {
        override val drugId: String = UUID.randomUUID().toString()
    }

    data object NoContent : DrugListItem {
        override val drugId: String = UUID.randomUUID().toString()
    }

    @Parcelize
    data class Drug(
        override val drugId: String = "",
        val name: String = "",
        val summary: String = ""
    ) : Parcelable, DrugListItem
}