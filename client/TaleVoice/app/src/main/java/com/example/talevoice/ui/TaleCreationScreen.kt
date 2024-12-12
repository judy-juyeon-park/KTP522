package com.example.talevoice.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.talevoice.TaleApplication
import com.example.talevoice.data.TaleStory
import com.example.talevoice.viewmodel.TaleIllustrationViewModel
import com.example.talevoice.viewmodel.TaleIllustrationViewModelFactory


@Composable
fun TaleCreationScreen(navController: NavHostController, taleCreation: TaleStory) {

    Log.d("TaleCreationScreen", "TaleCreationScreen called!!!!!")
    val repository = (LocalContext.current.applicationContext as TaleApplication).taleRepository
    val illustrationViewModel: TaleIllustrationViewModel = viewModel(
        factory = TaleIllustrationViewModelFactory(repository)
    )

    // TODO
    val illustrations by illustrationViewModel.illustrations.collectAsState()
    val firstImageLoaded by illustrationViewModel.firstImageLoaded.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("IllustrationViewModel", "Initial illustrations: ${illustrations}")
        Log.d("IllustrationViewModel", "Initial firstImageLoaded: ${firstImageLoaded}")
    }

    Log.d("TaleCreationScreen", "Illustrations: $illustrations")
    Log.d("TaleCreationScreen", "FirstImageLoaded: $firstImageLoaded.value")

    if (!firstImageLoaded) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    Log.d("TaleCreationScreen", "111")

    // Pager를 사용해 동화 스토리와 삽화를 함께 표시
    val pagerState = rememberPagerState(pageCount = { taleCreation.story.size })

    Box(Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 삽화 이미지 표시
                    illustrations.find { it.page == page + 1 }?.let { illustration ->
                        Log.d("TaleCreationScreen", "Page: ${page + 1}")
                        Log.d("TaleCreationScreen", "Illustration URL: ${illustration.image}")
                        AsyncImage(
                            model = illustration.image, // 로컬 이미지 경로 사용
                            contentDescription = "Illustration for Page ${page + 1}",
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .aspectRatio(1.0f)
                                .padding(16.dp)
                        )
                    }

                    // 동화 스토리 표시
                    Text(
                        text = taleCreation.story[page],
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }

        // Page Indicator
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(5.dp)
                )
            }
        }
    }
}
