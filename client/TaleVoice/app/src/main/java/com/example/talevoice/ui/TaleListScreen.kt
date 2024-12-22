package com.example.talevoice.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.talevoice.TaleApplication
import com.example.talevoice.TaleList
import com.example.talevoice.viewmodel.TaleIllustrationViewModel
import com.example.talevoice.viewmodel.TaleListViewModel
import com.example.talevoice.viewmodel.TaleListViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun TaleListScreen(
    navController: NavHostController,
    taleIllustrationViewModel: TaleIllustrationViewModel,
    name: String?,
    gender: String?
) {
    val repository = (LocalContext.current.applicationContext as TaleApplication).taleRepository
    val viewModel: TaleListViewModel = viewModel(
        factory = TaleListViewModelFactory(repository)
    )

    var isLoading by remember { mutableStateOf(false) }
    var isCreating by remember { mutableStateOf(false) }
    val taleList by viewModel.taleList.collectAsState()

    if (isCreating) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}) // 터치 차단
        )
        Dialog(onDismissRequest = { /* Do nothing */ }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${name ?: "사용자"}의 이야기를 만들고 있습니다.",
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Thin,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, bottom = 15.dp)
                    )
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(4.dp)
                    )
                }
            }
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
                    isCreating = true
                    Log.d("TaleListScreen", "Button clicked")

                    viewModel.viewModelScope.launch {
                        try {
                            isCreating = true
                            val safeName = name ?: "Unknown"
                            val safeGender = gender ?: "Unknown"
                            // 동화 내용 + 삽화 1개 리턴
                            val taleItem = viewModel.createTale(safeName, safeGender)
                            val prompts =  taleIllustrationViewModel.getIllustPrompts(taleItem, safeGender)
                            Log.d("TaleListScreen", "prompts ready")
                            taleIllustrationViewModel.fetchIllustrations(prompts)
                            taleIllustrationViewModel.illustrations.first { it.isNotEmpty() }
                            Log.d("TaleListScreen", "one image ready")

                            isCreating = false
                            navController.navigate(taleItem) {
                                popUpTo<TaleList>()
                            }

                        } catch (e: Exception){
                            isCreating = false
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

        if (isLoading && !isCreating) {
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