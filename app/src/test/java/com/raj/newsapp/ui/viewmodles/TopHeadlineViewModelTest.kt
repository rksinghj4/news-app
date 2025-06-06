package com.raj.newsapp.ui.viewmodles

import app.cash.turbine.test
import com.raj.newsapp.common.Constants.COUNTRY
import com.raj.newsapp.common.DispatcherProvider
import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.repository.NewsRepository
import com.raj.newsapp.ui.base.UiState
import com.raj.newsapp.utils.CustomMockData.article
import com.raj.newsapp.utils.CustomMockData.expectedEmptyListOfArticles
import com.raj.newsapp.utils.CustomMockData.expectedNonEmptyListOfArticles
import com.raj.newsapp.utils.TestDispatcherProvider
import com.raj.newsapp.viewmodel.TopHeadlineViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class TopHeadlineViewModelTest {
    @Mock
    lateinit var newsRepository: NewsRepository

    private lateinit var dispatcherProvider: DispatcherProvider

    //Don't use UnconfinedTestDispatcher, you will app.cash.turbine.TurbineAssertionError: No value produced in 3s
    private val standardTestDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        dispatcherProvider = TestDispatcherProvider(standardTestDispatcher)
    }

    @Test
    fun fetchTopHeadlines_whenRepositoryResponseSuccess_ShouldSetSuccessUIState_EmptyList() {
        //Same dispatcher must control runTest and TopHeadlineViewModel's coroutine and network calls.
        runTest(standardTestDispatcher) {
            doReturn(
                //What Repository method fetchTopHeadlines
                flow {
                    println("Emitting TopHeadlinesResponse")
                    emit(
                        //What Webservice returns
                        TopHeadlinesResponse(
                            articles = expectedEmptyListOfArticles,
                            status = "ok",
                            totalResults = 0
                        )
                    )
                }).`when`(newsRepository)
                .fetchTopHeadlines(COUNTRY)//Must always pass data class object or Primitive types

            //Create your own TopHeadlineViewModel manually. No mocking for what we are testing.
            val viewModel = TopHeadlineViewModel(newsRepository, dispatcherProvider)
            //fetchTopHeadlines is called On TopHeadlineViewModel's init{fetchTopHeadlines()}

            /**
             * If you don't want to write: Skips the initial emission (e.g., UiState.Loading) or assertEquals assertEquals(UiState.Loading, awaitItem())
             *
             * Then Advance the test dispatcher to process coroutines
             * This ensures that any coroutines launched in the ViewModel’s init block or elsewhere
             * are processed before Turbine tries to collect emissions.
             */
            //standardTestDispatcher.scheduler.advanceUntilIdle() //Sufficient time dega api call ko, Jis se ki uiStateFlow api response k sath update ho jaye

            // Assert: Use Turbine to collect emissions from the StateFlow
            viewModel.uiStateFlow.test {
                //1.  First awaitItem() will receive the initial state (e.g., UiState.Loading)
                //We skip it because we're interested in the *result* of the operation
                skipItems(1) // Skips the initial emission (e.g., UiState.Loading) or assertEquals assertEquals(UiState.Loading, awaitItem())
                //assertEquals(UiState.Loading, awaitItem())

                // 2. Now, this awaitItem() will receive the state *after* the fetch operation completes
                val success = awaitItem()
                assertEquals(UiState.Success(expectedEmptyListOfArticles), success)
                // 3. Cancel the collection as we don't expect more items.
                cancelAndIgnoreRemainingEvents()
            }

            /**
             * Verify that the repository(dependency ka) method was called exactly once with the correct country,
             * during merge conflict there is a chance of duplicate calls.
             */
            verify(newsRepository, times(1)).fetchTopHeadlines(COUNTRY)
        }
    }

    @Test
    fun fetchTopHeadlines_whenRepositoryResponseSuccess_ShouldSetSuccessUIState_NonEmptyList() {
        runTest(standardTestDispatcher) {
            doReturn(
                flow {
                    emit(
                        TopHeadlinesResponse(
                            articles = expectedNonEmptyListOfArticles,
                            status = "ok",
                            totalResults = expectedEmptyListOfArticles.size
                        )
                    )
                }
            ).`when`(newsRepository).fetchTopHeadlines(COUNTRY)

            val viewModel = TopHeadlineViewModel(newsRepository, dispatcherProvider)
            standardTestDispatcher.scheduler.advanceUntilIdle()//api se result ane do
            viewModel.uiStateFlow.test {
                //skipItems(1)
                val successResult =
                    awaitItem()//value collected in viewmodel and updated in uiStateFlow
                assertEquals(UiState.Success(expectedNonEmptyListOfArticles), successResult)
                cancelAndIgnoreRemainingEvents()
            }
            verify(newsRepository, times(1)).fetchTopHeadlines(COUNTRY)
        }
    }


    @Test
    fun fetchTopHeadlines_whenRepositoryResponseSuccess_ShouldSetSuccessUIState_NonEmptyList_DataCorruption() {
        runTest(standardTestDispatcher) {
            doReturn(
                flow {
                    emit(
                        TopHeadlinesResponse(
                            articles = expectedNonEmptyListOfArticles,
                            status = "ok",
                            totalResults = expectedNonEmptyListOfArticles.size
                        )
                    )
                }
            ).`when`(newsRepository).fetchTopHeadlines(COUNTRY)

            val viewModel = TopHeadlineViewModel(newsRepository, dispatcherProvider)
            standardTestDispatcher.scheduler.advanceUntilIdle()//api se result ane do
            val listSizeBeforeModification =
                UiState.Success(expectedNonEmptyListOfArticles).data.size //2
            //Here: You you can Modify in domain layer also

            viewModel.uiStateFlow.test {
                //skipItems(1)
                //E.g. Modifying the data in viewmodel
                expectedNonEmptyListOfArticles.add(article)
                val successResult =
                    awaitItem() as UiState.Success //value collected in viewmodel and updated in uiStateFlow
                assertNotEquals(listSizeBeforeModification, successResult.data.size) // 2 != 3
                cancelAndIgnoreRemainingEvents()
            }
            verify(newsRepository, times(1)).fetchTopHeadlines(COUNTRY)
        }
    }

    @Test
    fun fetchTopHeadlines_whenRepositoryResponseError_ShouldSetErrorUIState_Match() {
        runTest(standardTestDispatcher) {
            //If error message is different from viewmodel then Expected != Actual
            val errorMessage = "Something went wrong"
            doReturn(
                flow {
                    emit(
                        TopHeadlinesResponse(
                            articles = expectedEmptyListOfArticles,
                            status = "error_code",
                            totalResults = expectedEmptyListOfArticles.size
                        )
                    )
                }
            ).`when`(newsRepository).fetchTopHeadlines(COUNTRY)
            //Create and call subject under test
            val viewMode = TopHeadlineViewModel(newsRepository, dispatcherProvider)

            viewMode.uiStateFlow.test {
                skipItems(1)
                assertEquals(UiState.Error(errorMessage), awaitItem())
            }
            verify(newsRepository, times(1)).fetchTopHeadlines(COUNTRY)
        }
    }

    @Test
    fun fetchTopHeadlines_whenRepositoryResponseError_ShouldSetErrorUIState_UnMatch() {
        runTest(standardTestDispatcher) {
            //If error message is different from viewmodel then Expected != Actual
            val errorMessage = "Something went wrong, differ message here"
            doReturn(
                flow {
                    emit(
                        TopHeadlinesResponse(
                            articles = expectedEmptyListOfArticles,
                            status = "error_code",
                            totalResults = expectedEmptyListOfArticles.size
                        )
                    )
                }
            ).`when`(newsRepository).fetchTopHeadlines(COUNTRY)

            val viewMode = TopHeadlineViewModel(newsRepository, dispatcherProvider)

            viewMode.uiStateFlow.test {
                skipItems(1)
                assertNotEquals(UiState.Error(errorMessage), awaitItem())
            }
            verify(newsRepository, times(1)).fetchTopHeadlines(COUNTRY)
        }
    }

    @After
    fun tearDown() {
        //Clean up if needed.
    }
}