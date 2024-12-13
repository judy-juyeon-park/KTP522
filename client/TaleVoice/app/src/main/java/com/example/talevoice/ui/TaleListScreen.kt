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
import com.example.talevoice.viewmodel.TaleIllustrationViewModel
import com.example.talevoice.viewmodel.TaleStoryViewModel
import com.example.talevoice.viewmodel.TaleCreationViewModelFactory
import com.example.talevoice.viewmodel.TaleIllustrationViewModelFactory
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
    val creationViewModel: TaleStoryViewModel = viewModel(
        factory = TaleCreationViewModelFactory(repository)
    )
    val taleIllustrationViewModel: TaleIllustrationViewModel = viewModel(
        factory = TaleIllustrationViewModelFactory(repository)
    )

    var isLoading by remember { mutableStateOf(false) }
    val taleList by viewModel.taleList.collectAsState()
    val createdTale by creationViewModel.createdTale.collectAsState()
    val navigateToTaleCreation by taleIllustrationViewModel.navigateToTaleCreation.collectAsState()

    // 생성된 동화가 있으면 동화 삽화 생성 요청
    LaunchedEffect(createdTale) {
        Log.d("TaleListScreen", "LaunchedEffect by createdTale!")
        createdTale?.let { tale ->
            val requests = tale.story.mapIndexed { index, text ->
                IllustPrompt(
                    page = index + 1,
                    paragraph = text,
                    gender = gender.toString()
                )
            }
            taleIllustrationViewModel.fetchIllustrations(requests)
        }
    }

    // 첫 번째 삽화 로드 후 화면 출력
    LaunchedEffect(navigateToTaleCreation) {
        Log.d("TaleListScreen", "LaunchedEffect by navigateToTaleCreation!")
        if (navigateToTaleCreation) {
            val tale = creationViewModel.getCreatedTaleItem()
            val jsonString = Json.encodeToString(tale)
            val safeJsonString = URLEncoder.encode(jsonString, "UTF-8")
            navController.navigate("TaleCreationScreen/$safeJsonString") {
                popUpTo<TaleList>()
            }

            isLoading = false
            creationViewModel.resetCreatedTale()
            taleIllustrationViewModel.resetNavigationState()
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
                    isLoading = true // 로딩 시작
                    Log.d("TaleListScreen", "Button clicked")

                    viewModel.viewModelScope.launch {
                        try {
                            val safeName = name ?: "Unknown"
                            val safeGender = gender ?: "Unknown"
                            // 동화 내용 + 삽화 1개 리턴
                            val taleItem = creationViewModel.createTale(safeName, safeGender)
                            isLoading = false
                            navController.navigate(taleItem) {
                                popUpTo<TaleList>()
                            }
                        } catch (e: Exception){
                            isLoading = false
                            println("Error fetching tale details: ${e.message}")
                        }
                    }


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