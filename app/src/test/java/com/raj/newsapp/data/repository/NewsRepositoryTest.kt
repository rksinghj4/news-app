package com.raj.newsapp.data.repository

import app.cash.turbine.test
import com.raj.newsapp.common.Constants.COUNTRY
import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.repository.NewsRepositoryImpl
import com.raj.newsapp.model.webservice.WebService
import com.raj.newsapp.utils.CustomMockData.expectedEmptyListOfArticles
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class NewsRepositoryTest {
    @Mock
    lateinit var webService: WebService
    //val standardTestDispatcher = StandardTestDispatcher()
    //private lateinit var testDispatcherProvider: TestDispatcherProvider

    @Before
    fun setUP() {
        MockitoAnnotations.openMocks(this)
        //testDispatcherProvider = TestDispatcherProvider(standardTestDispatcher)
    }

    @Test
    fun fetchTopHeadlines_WhenWebServiceResponseSuccess_ReturnFlowOfTopHeadlinesResponse() {
        runTest {
            val topHeadlinesResponse = TopHeadlinesResponse(
                articles = expectedEmptyListOfArticles,
                status = "ok",
                totalResults = 0
            )
            doReturn(
                topHeadlinesResponse
            ).`when`(webService).fetchTopHeadlines(COUNTRY)
            //Testing it, Create manually and call it's method which in turn call webService.fetchTopHeadlines
            val newsRepository = NewsRepositoryImpl(webService)
                newsRepository.fetchTopHeadlines(COUNTRY).test {
                assertEquals(topHeadlinesResponse, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            /**
             * Verify that the webService(dependency ka) method was called exactly once with the correct country,
             * during merge conflict there is a chance of duplicate calls.
             */
            verify(webService, times(1)).fetchTopHeadlines(COUNTRY)
        }
    }

    @Test
    fun getTopHeadlines_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val errorMessage = "Error Message For Testing"
            doThrow(RuntimeException(errorMessage))
                .`when`(webService)
                .fetchTopHeadlines(COUNTRY) //dependency se jo chahiye usko mock karo
            //Iska object create karo
            val newsRepository = NewsRepositoryImpl(webService)
            //iska method call karo
            newsRepository.fetchTopHeadlines(COUNTRY).test {
                assertEquals(errorMessage, awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            ////dependency k method ko ik hi bar call kiya h.
            verify(webService, times(1)).fetchTopHeadlines(COUNTRY)
        }
    }

    @After
    fun tearDown() {

    }
}

