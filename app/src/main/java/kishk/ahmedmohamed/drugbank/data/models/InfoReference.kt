package kishk.ahmedmohamed.drugbank.data.models

import java.util.UUID

sealed interface InfoReferenceListItem {
    val referenceId: String

    data class InfoReference(
        override val referenceId: String = "",
        val details: String = ""
    ) : InfoReferenceListItem

    data object Loading : InfoReferenceListItem {
        override val referenceId: String = UUID.randomUUID().toString()
    }

    data object NoContent : InfoReferenceListItem {
        override val referenceId: String = UUID.randomUUID().toString()
    }
}