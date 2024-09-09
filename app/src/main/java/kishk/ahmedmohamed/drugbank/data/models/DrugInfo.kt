package kishk.ahmedmohamed.drugbank.data.models

import java.util.UUID

sealed interface DrugInfoListItem {
    val name: String

    data class DrugInfo(
        override val name: String,
        val value: String
    ) : DrugInfoListItem

    data object Loading : DrugInfoListItem {
        override val name: String = UUID.randomUUID().toString()
    }

    data object NoContent : DrugInfoListItem {
        override val name: String = UUID.randomUUID().toString()
    }

}

data class DrugInfo(
    val id: String = "",
    val name: String = "",
    val summary: String = "",
    val background: String = "",
    val pharmacologyIndication: String = "",
    val pharmacodynamics: String = "",
    val mechanismOfAction: String = "",
    val absorption: String = "",
    val proteinBinding: String = "",
    val metabolism: String = "",
    val routeOfElimination: String = "",
    val toxicity: String = ""
) {
    fun toListItem(): List<DrugInfoListItem.DrugInfo> {
        return listOf(
            DrugInfoListItem.DrugInfo("Background", background),
            DrugInfoListItem.DrugInfo("Indication", pharmacologyIndication),
            DrugInfoListItem.DrugInfo("Pharmacodynamics", pharmacodynamics),
            DrugInfoListItem.DrugInfo("Mechanism of action", mechanismOfAction),
            DrugInfoListItem.DrugInfo("Absorption", absorption),
            DrugInfoListItem.DrugInfo("Protein binding", proteinBinding),
            DrugInfoListItem.DrugInfo("Metabolism", metabolism),
            DrugInfoListItem.DrugInfo("Route of elimination", routeOfElimination),
            DrugInfoListItem.DrugInfo("Toxicity", toxicity)
        )
    }
}