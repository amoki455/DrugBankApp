package kishk.ahmedmohamed.drugbank.data.repositories

import kishk.ahmedmohamed.drugbank.data.models.DrugInfo
import kishk.ahmedmohamed.drugbank.data.models.DrugInfoReference
import kishk.ahmedmohamed.drugbank.data.models.DrugListItem
import kishk.ahmedmohamed.drugbank.data.models.InfoReferenceListItem
import kishk.ahmedmohamed.drugbank.data.models.Response

interface DrugRepository {
    suspend fun getDrugs(limit: Long = Long.MAX_VALUE): Response<List<DrugListItem.Drug>>
    suspend fun getDrugs(limit: Long = Long.MAX_VALUE, startAfterId: String): Response<List<DrugListItem.Drug>>
    suspend fun getDrugInfo(drugId: String): Response<DrugInfo?>
    suspend fun getDrugReferences(drugId: String): Response<List<DrugInfoReference>>
    suspend fun getReferences(ids: List<String>): Response<List<InfoReferenceListItem.InfoReference>>
}