package com.raj.newsapp.utils

import com.raj.newsapp.model.data.TopHeadlinesResponse.Article

object CustomMockData {
    val article = Article(
        author = "Sudhir",
        content = "This is a Unit testing content",
        description = "",
        publishedAt = "2025-06-06T08:54:00.000Z",//Separator between date and time and Z: Indicates UTC time (Zulu time)
        source = Article.Source(
            id = "123",
            name = "Times Now"
        ),
        title = "",
        url = "",
        urlToImage = ""
    )
    val expectedEmptyListOfArticles = emptyList<Article>()
    val expectedNonEmptyListOfArticles = mutableListOf<Article>(
        Article(
            author = "Sudhir",
            content = "This is a Unit testing content",
            description = "",
            publishedAt = "2025-06-06T08:54:00.000Z",//Separator between date and time and Z: Indicates UTC time (Zulu time)
            source = Article.Source(
                id = "123",
                name = "Times Now"
            ),
            title = "",
            url = "",
            urlToImage = ""
        ),
        Article(
            author = "Sankalp",
            content = "This is a Unit testing content",
            description = "",
            publishedAt = "2025-06-06T08:54:00.000Z",//Separator between date and time and Z: Indicates UTC time (Zulu time)
            source = Article.Source(
                id = "124",
                name = "Hindustan"
            ),
            title = "",
            url = "",
            urlToImage = ""
        )
    )


}