package com.example.talevoice.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.talevoice.TaleApplication
import com.example.talevoice.TaleList
import com.example.talevoice.data.IllustPrompt
import com.example.talevoice.viewmodel.IllustrationViewModel
import com.example.talevoice.viewmodel.IllustrationViewModelFactory
import com.example.talevoice.viewmodel.TaleCreationViewModel
import com.example.talevoice.viewmodel.TaleCreationViewModelFactory
import com.example.talevoice.viewmodel.TaleListViewModel
import com.example.talevoice.viewmodel.TaleListViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

@Composable
fun TaleListScreen(navController: NavHostController, name: String?, gender: String?) {
    val repository = (LocalContext.current.applicationContext as TaleApplication).taleRepository
    val viewModel: TaleListViewModel = viewModel(
        factory = TaleListViewModelFactory(repository)
    )
    val creationViewModel: TaleCreationViewModel = viewModel(
        factory = TaleCreationViewModelFactory(repository)
    )
    val illustrationViewModel: IllustrationViewModel = viewModel(
        factory = IllustrationViewModelFactory(repository)
    )

    val taleList by viewModel.taleList.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    val createdTale by creationViewModel.createdTale.collectAsState() // 생성된 동화 감지

    // 생성된 동화가 있으면 동화 삽화 생성 요청
    LaunchedEffect(createdTale) {
        Log.d("TaleListScreen", "LaunchedEffect!")
        createdTale?.let { tale ->
            // 요청 생성
            val requests = tale.story.mapIndexed { index, text ->
                IllustPrompt(
                    page = index + 1,
                    paragraph = text,
                    gender = gender.toString()
                )
            }
            illustrationViewModel.fetchIllustrations(requests)
            /* val taleCreation = creationViewModel.getCreatedTaleItem()
             navController.navigate(taleCreation.toString()) {
                 popUpTo<TaleList>()
             }*/
            // Json 시도
            /*val jsonString = Json.encodeToString(tale)
            val safeJsonString = URLEncoder.encode(jsonString, "UTF-8")
            navController.navigate("TaleCreationScreen/$safeJsonString") {
                popUpTo<TaleList>()
            }

            isLoading = false
            creationViewModel.resetCreatedTale()*/
        }
    }

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "안녕, $name",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Thin,
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = {
                    val safeName = name ?: "Unknown"
                    val safeGender = gender ?: "Unknown"
                    Log.d("TaleListScreen", "Button clicked")
                    creationViewModel.createTale(safeName, safeGender)
                    isLoading = true // 로딩 시작
                },
                modifier = Modifier.fillMaxWidth(),

            ) {
                Text(
                    text = "나만의 동화 생성",
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Thin
                )
            }
        }

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        }
        LazyColumn {
            itemsIndexed(taleList) { index, tale ->
                Text(text = tale.title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Thin,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isLoading) {
                                return@clickable
                            }
                            isLoading = true
                            viewModel.viewModelScope.launch {
                                try {
                                    val taleDetail = viewModel.getTaleDetail(tale.taleId)
                                    Log.d("TaleListScreen", tale.taleId)
                                    isLoading = false
                                    navController.navigate(taleDetail) {
                                        popUpTo<TaleList>()
                                    }
                                } catch (e: Exception) {
                                    isLoading = false
                                    println("Error fetching tale details: ${e.message}")
                                }
                            }
                        }
                        .padding(16.dp))
                if (index < taleList.size - 1) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}