package com.example.talevoice.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.talevoice.TaleApplication
import com.example.talevoice.viewmodel.TaleListViewModel
import com.example.talevoice.viewmodel.TaleListViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TaleListScreen(){
    val repository = (LocalContext.current.applicationContext as TaleApplication).taleRepository
    val viewModel: TaleListViewModel = viewModel(
        factory = TaleListViewModelFactory(repository)
    )

    val taleList by viewModel.taleList.collectAsState()
    LazyColumn {
        items(taleList) { tale ->
            Text(text = tale.title)
        }
    }

}