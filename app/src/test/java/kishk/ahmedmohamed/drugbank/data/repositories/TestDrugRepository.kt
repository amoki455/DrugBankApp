package kishk.ahmedmohamed.drugbank.data.repositories

import kishk.ahmedmohamed.drugbank.data.models.DrugInfo
import kishk.ahmedmohamed.drugbank.data.models.DrugInfoReference
import kishk.ahmedmohamed.drugbank.data.models.DrugListItem
import kishk.ahmedmohamed.drugbank.data.models.InfoReferenceListItem
import kishk.ahmedmohamed.drugbank.data.models.Response
import javax.inject.Inject

class TestDrugRepository @Inject constructor() : DrugRepository {

    companion object {
        val testException = Throwable("Test exception")
    }

    var throwException = false
    var returnSuccess = true
    var returnEmpty = false

    override suspend fun getDrugs(limit: Long): Response<List<DrugListItem.Drug>> {
        if (throwException)
            throw testException

        return if (returnSuccess) {
            if (returnEmpty) {
                Response.Success(emptyList())
            } else {
                Response.Success(
                    mutableListOf<DrugListItem.Drug>().apply {
                        repeat(limit.toInt()) {
                            add(DrugListItem.Drug(it.toString(), "drug $it", "summary $it"))
                        }
                    }
                )
            }
        } else {
            Response.Failure(testException)
        }
    }

    override suspend fun getDrugs(
        limit: Long,
        startAfterId: String
    ): Response<List<DrugListItem.Drug>> {
        return getDrugs(limit)
    }

    override suspend fun getDrugInfo(drugId: String): Response<DrugInfo?> {
        if (throwException)
            throw testException

        return if (returnSuccess) {
            if (returnEmpty) {
                Response.Success(null)
            } else {
                Response.Success(DrugInfo(drugId, "drug $drugId"))
            }
        } else {
            Response.Failure(testException)
        }
    }

    override suspend fun getDrugReferences(drugId: String): Response<List<DrugInfoReference>> {
        if (throwException)
            throw testException

        return if (returnSuccess) {
            if (returnEmpty) {
                Response.Success(emptyList())
            } else {
                Response.Success(
                    listOf(
                        DrugInfoReference(drugId, 1, "refId0"),
                        DrugInfoReference(drugId, 2, "refId1"),
                    )
                )
            }
        } else {
            Response.Failure(testException)
        }
    }

    override suspend fun getReferences(ids: List<String>): Response<List<InfoReferenceListItem.InfoReference>> {
        if (throwException)
            throw testException

        return if (returnSuccess) {
            if (returnEmpty) {
                return Response.Success(emptyList())
            } else {
                return Response.Success(
                    listOf(
                        InfoReferenceListItem.InfoReference("refId0", "reference details"),
                        InfoReferenceListItem.InfoReference("refId1", "reference details"),
                    )
                )
            }
        } else {
            Response.Failure(testException)
        }
    }
}