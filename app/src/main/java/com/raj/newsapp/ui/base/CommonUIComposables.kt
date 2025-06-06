package com.raj.newsapp.ui.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.raj.newsapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScaffold(title: String, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(text = title)
                }
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier.padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    content()
                }
            }
        }
    )
}

@Composable
fun Loading() {
    val contentDesc = stringResource(R.string.loading)
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier
            .align(Alignment.Center)
            .semantics {
                contentDescription = contentDesc
            })
    }
}

@Composable
fun ErrorScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.Center),
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Red,
        )
    }
}