package kishk.ahmedmohamed.drugbank.data.repositories

import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kishk.ahmedmohamed.drugbank.data.models.DrugInfo
import kishk.ahmedmohamed.drugbank.data.models.DrugInfoReference
import kishk.ahmedmohamed.drugbank.data.models.DrugListItem
import kishk.ahmedmohamed.drugbank.data.models.InfoReferenceListItem
import kishk.ahmedmohamed.drugbank.data.models.Response
import kishk.ahmedmohamed.drugbank.di.FireStoreDrugReferencesCollection
import kishk.ahmedmohamed.drugbank.di.FireStoreDrugsCollection
import kishk.ahmedmohamed.drugbank.di.FireStoreDrugsInfoCollection
import kishk.ahmedmohamed.drugbank.di.FireStoreReferencesInfoCollection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreDrugRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val fireStoreDb: FirebaseFirestore,
    @FireStoreDrugsCollection private val drugsCollection: String,
    @FireStoreDrugsInfoCollection private val drugsInfoCollection: String,
    @FireStoreDrugReferencesCollection private val drugReferencesCollection: String,
    @FireStoreReferencesInfoCollection private val referencesInfoCollection: String,
) : DrugRepository {
    override suspend fun getDrugs(limit: Long): Response<List<DrugListItem.Drug>> =
        withContext(dispatcher) {
            try {
                Response.Success(
                    fireStoreDb.collection(drugsCollection)
                        .orderBy("drugId")
                        .limit(limit)
                        .get()
                        .await()
                        .toObjects(DrugListItem.Drug::class.java)
                )
            } catch (e: Throwable) {
                Response.Failure(e)
            }
        }

    override suspend fun getDrugs(
        limit: Long,
        startAfterId: String
    ): Response<List<DrugListItem.Drug>> =
        withContext(dispatcher) {
            try {
                Response.Success(
                    fireStoreDb.collection(drugsCollection)
                        .orderBy("drugId")
                        .startAfter(startAfterId)
                        .limit(limit)
                        .get()
                        .await()
                        .toObjects(DrugListItem.Drug::class.java)
                )
            } catch (e: Throwable) {
                Response.Failure(e)
            }
        }

    override suspend fun getDrugInfo(drugId: String): Response<DrugInfo?> =
        withContext(dispatcher) {
            try {
                Response.Success(
                    fireStoreDb.collection(drugsInfoCollection)
                        .whereEqualTo("id", drugId)
                        .get()
                        .await()
                        .firstOrNull()
                        ?.toObject(DrugInfo::class.java)
                )
            } catch (e: Throwable) {
                Response.Failure(e)
            }
        }

    override suspend fun getDrugReferences(drugId: String): Response<List<DrugInfoReference>> =
        withContext(dispatcher) {
            try {
                Response.Success(
                    fireStoreDb.collection(drugReferencesCollection)
                        .whereEqualTo("drugId", drugId)
                        .get()
                        .await()
                        .toObjects(DrugInfoReference::class.java)
                )
            } catch (e: Throwable) {
                Response.Failure(e)
            }
        }

    override suspend fun getReferences(ids: List<String>): Response<List<InfoReferenceListItem.InfoReference>> =
        withContext(dispatcher) {
            try {
                Response.Success(
                    fireStoreDb.collection(referencesInfoCollection)
                        .where(Filter.inArray("referenceId", ids))
                        .get()
                        .await()
                        .toObjects(InfoReferenceListItem.InfoReference::class.java)
                )
            } catch (e: Throwable) {
                Response.Failure(e)
            }
        }
}