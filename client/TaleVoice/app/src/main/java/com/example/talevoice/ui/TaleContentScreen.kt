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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.talevoice.data.TaleItem
import coil3.compose.AsyncImage
import com.example.talevoice.R
import com.example.talevoice.TaleApplication
import com.example.talevoice.viewmodel.TaleDetailViewModel
import com.example.talevoice.viewmodel.TaleDetailViewModelFactory
import com.example.talevoice.viewmodel.TaleListViewModel
import com.example.talevoice.viewmodel.TaleListViewModelFactory

@Composable
fun TaleContentScreen(taleItem: TaleItem, navController: NavHostController, modifier: Modifier) {
    Log.d("TaleContentScreen", taleItem.toString())

    // TODO("Impl TTS")
    val ttsApiService = (LocalContext.current.applicationContext as TaleApplication).ttsApiService
    val viewModel: TaleDetailViewModel = viewModel(
        factory = TaleDetailViewModelFactory(ttsApiService)
    )

    val pagerState = rememberPagerState(pageCount = {
        taleItem.context.size
    })
    Box(Modifier.fillMaxSize()) {


        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Our page content
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    Box(
                        Modifier
                            .fillMaxWidth(0.6f)
                            .aspectRatio(1.0f)
                            .padding(15.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        AsyncImage(
                            model = taleItem.image[page],
                            contentDescription = "Image",
                            error = painterResource(R.drawable.placeholder),
                            placeholder = painterResource(R.drawable.placeholder),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    Box(Modifier.fillMaxWidth(0.9f)) {
                        Text(
                            text = taleItem.context[page],
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                }

            }
        }
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

        // TODO("Add Floating Action Button to right bottom screen")
        // Voice 데이터 로딩 중일 때는 CircleProgress 표시
        // Voice 실행 가능 하면 실행 버튼 아이콘
        // Voice 실행 중에는 멈품 버튼 아이콘
        // https://fonts.google.com/icons?selected=Material+Symbols+Outlined:play_arrow:FILL@1;wght@300;GRAD@0;opsz@40&icon.query=play&icon.size=24&icon.color=%235f6368&icon.platform=android
        // 아래 코드 참조
        /*
        IconButton(onClick = {

                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
         */
    }
}
