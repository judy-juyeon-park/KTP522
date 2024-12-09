package com.example.talevoice.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.talevoice.TaleApplication
import com.example.talevoice.TaleList
import com.example.talevoice.viewmodel.TaleCreationViewModel
import com.example.talevoice.viewmodel.TaleCreationViewModelFactory
import com.example.talevoice.viewmodel.TaleListViewModel
import com.example.talevoice.viewmodel.TaleListViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun TaleListScreen(navController: NavHostController, userName: String?, gender: String?) {
    val repository = (LocalContext.current.applicationContext as TaleApplication).taleRepository
    val viewModel: TaleListViewModel = viewModel(
        factory = TaleListViewModelFactory(repository)
    )
    val creationViewModel: TaleCreationViewModel = viewModel(
        factory = TaleCreationViewModelFactory(repository)
    )

    val taleList by viewModel.taleList.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    val isCreating by creationViewModel.isCreatingTale.collectAsState()
/*    val taleCreation by viewModel.taleCreationResult.collectAsState()
    val talePrompts by viewModel.talePromptsResult.collectAsState()

    // talePrompts가 업데이트되면
    LaunchedEffect(talePrompts) {
        talePrompts?.let {
            // 동화 생성 완료 후 필요한 작업 수행
            // 예를 들어 생성 완료 후 TaleList를 다시 표시하거나 새로운 화면으로 이동 가능
            isLoading = false
            navController.navigate("TaleCreationScreen")
        }
    }*/

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "안녕, ${userName}",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Thin,
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = {

                    //creationViewModel.createTale() // 버튼 클릭 시 동화 생성 API 호출
                    isLoading = true // 로딩 시작
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isCreating
            ) {
                Text(
                    text = if (isCreating) "생성 중..." else "나만의 동화 생성",
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
        // 로딩 오버레이: 동화 생성 중일 때 반투명 배경 + 메시지 + Progress Indicator
        if (isCreating) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // 반투명 배경
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Text(
                        text = "${userName}의 이야기를 만들고 있습니다...",
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Thin,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}