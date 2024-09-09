package kishk.ahmedmohamed.drugbank.ui.fragments.references

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kishk.ahmedmohamed.drugbank.data.repositories.TestDrugRepository
import kishk.ahmedmohamed.drugbank.observeLiveDataUntil
import kishk.ahmedmohamed.drugbank.observeLiveDataValueChanges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReferencesListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: TestDrugRepository
    private lateinit var viewModel: ReferencesListViewModel

    @Before
    fun setup() {
        repository = TestDrugRepository()
        viewModel = ReferencesListViewModel(repository, "drug1")
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When drugId is empty, noContent state is true`() {
        runTest {
            viewModel = ReferencesListViewModel(repository, "")
            viewModel.load()
            advanceUntilIdle()
            val result = viewModel.noContent.value
            assertEquals(true, result)
        }
    }

    @Test
    fun `When data is empty, noContent state is true`() {
        runTest {
            repository.returnEmpty = true
            viewModel.load()
            advanceUntilIdle()
            val result = viewModel.noContent.value
            assertEquals(true, result)
        }
    }

    @Test
    fun `When loaded successfully, data is not empty`() {
        runTest {
            viewModel.load()
            advanceUntilIdle()
            val result = viewModel.data.value?.isNotEmpty()
            assertEquals(true, result)
        }
    }

    @Test
    fun `When loaded successfully, noContent state is false`() {
        runTest {
            viewModel.load()
            advanceUntilIdle()
            val result = viewModel.noContent.value
            assertEquals(false, result)
        }
    }

    @Test
    fun `Error state should be non null if there is an exception`() {
        runTest {
            repository.throwException = true
            observeLiveDataUntil(
                liveData = viewModel.error,
                condition = { it != null }
            ) {
                viewModel.load()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `Error state should be non null if return if failure`() {
        runTest {
            repository.returnSuccess = false
            observeLiveDataUntil(
                liveData = viewModel.error,
                condition = { it != null }
            ) {
                viewModel.load()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `clearErrorState should set error to null`() {
        runTest {
            repository.returnSuccess = false
            observeLiveDataValueChanges(
                liveData = viewModel.error,
                valuesSequence = listOf(null, TestDrugRepository.testException, null)
            ) {
                viewModel.load()
                advanceUntilIdle()
                viewModel.clearErrorState()
            }
        }
    }
}