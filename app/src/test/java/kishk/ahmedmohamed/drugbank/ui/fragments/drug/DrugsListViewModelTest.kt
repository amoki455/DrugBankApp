package kishk.ahmedmohamed.drugbank.ui.fragments.drug

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kishk.ahmedmohamed.drugbank.data.repositories.TestDrugRepository
import kishk.ahmedmohamed.drugbank.getOrAwaitValue
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
class DrugsListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: TestDrugRepository
    private lateinit var viewModel: DrugsListViewModel
    private val pageSize = 10

    @Before
    fun setup() {
        repository = TestDrugRepository()
        viewModel = DrugsListViewModel(repository, pageSize.toLong())
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Loading next page should set loading state to true before completion`() {
        runTest {
            observeLiveDataValueChanges(
                liveData = viewModel.isLoading,
                valuesSequence = listOf(false, true)
            ) {
                viewModel.loadNextPage()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `Loading next page should set loading state to false after completion`() {
        runTest {
            observeLiveDataValueChanges(
                liveData = viewModel.isLoading,
                valuesSequence = listOf(false, true, false)
            ) {
                viewModel.loadNextPage()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `Loading next page while loading state is true should return without loading`() {
        runTest {
            observeLiveDataUntil(
                liveData = viewModel.isLoading,
                condition = {
                    if (it) {
                        viewModel.loadNextPage()
                        advanceUntilIdle()
                        true
                    } else false
                }
            ) {
                viewModel.loadNextPage()
                advanceUntilIdle()
            }

            // Only one page is loaded
            assertEquals(pageSize, viewModel.items.getOrAwaitValue().size)
        }
    }

    @Test
    fun `Loading next page should return loaded items`() {
        runTest {
            observeLiveDataUntil(
                liveData = viewModel.items,
                condition = { it.size == pageSize }
            ) {
                viewModel.loadNextPage()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `Loading next page should add new items to the list`() {
        runTest {
            observeLiveDataUntil(
                liveData = viewModel.items,
                condition = { it.size == pageSize * 2 }
            ) {
                viewModel.loadNextPage()
                advanceUntilIdle()
                viewModel.loadNextPage()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `Loading empty page should indicate that all data has been loaded`() {
        runTest {
            repository.returnEmpty = true
            observeLiveDataValueChanges(
                liveData = viewModel.loadedAllData,
                valuesSequence = listOf(false, true)
            ) {
                viewModel.loadNextPage()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `Reloading should reset loadedAllData state`() {
        runTest {
            repository.returnEmpty = true
            observeLiveDataValueChanges(
                liveData = viewModel.loadedAllData,
                valuesSequence = listOf(false, true, false)
            ) {
                viewModel.loadNextPage()
                advanceUntilIdle()
                viewModel.reload()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `Reloading should cancel current loading job`() {
        runTest {
            observeLiveDataValueChanges(
                liveData = viewModel.isLoading,
                valuesSequence = listOf(false, true, false, true, false),
                onValue = { i, v ->
                    if (i == 1 && v) {
                        viewModel.reload()
                        advanceUntilIdle()
                    }
                }
            ) {
                viewModel.loadNextPage()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `Reloading should reset items to first page`() {
        runTest {
            observeLiveDataUntil(
                liveData = viewModel.items,
                condition = { it.size == pageSize * 2 }
            ) {
                viewModel.loadNextPage()
                advanceUntilIdle()
                viewModel.loadNextPage()
                advanceUntilIdle()
            }
            observeLiveDataUntil(
                liveData = viewModel.items,
                condition = { it.size == pageSize }
            ) {
                viewModel.reload()
                advanceUntilIdle()
            }
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
                viewModel.loadNextPage()
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
                viewModel.loadNextPage()
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
                viewModel.loadNextPage()
                advanceUntilIdle()
                viewModel.clearErrorState()
            }
        }
    }
}