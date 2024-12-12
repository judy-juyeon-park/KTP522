package com.example.talevoice.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.talevoice.TaleApplication
import com.example.talevoice.data.TaleCreation
import com.example.talevoice.viewmodel.IllustrationViewModel
import com.example.talevoice.viewmodel.IllustrationViewModelFactory


@Composable
fun TaleCreationScreen(navController: NavHostController, taleCreation: TaleCreation) {
    Log.d("TaleCreationScreen", taleCreation.toString())

    val repository = (LocalContext.current.applicationContext as TaleApplication).taleRepository
    val illustrationViewModel: IllustrationViewModel = viewModel(
        factory = IllustrationViewModelFactory(repository)
    )

    val illustrations by illustrationViewModel.illustrations.collectAsState()
    val isLoading by illustrationViewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val firstImageLoaded = illustrationViewModel.firstImageLoaded.collectAsState()

 /*   // Observe the first image
    LaunchedEffect(illustrations) {
        if (illustrations.isNotEmpty() && !firstImageLoaded.value) {
            firstImageLoaded.value = true
        }
    }*/

    if (!firstImageLoaded.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { taleCreation.story.size })

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
                    /*Box(
                        Modifier
                            .fillMaxWidth(0.6f)
                            .aspectRatio(1.0f)
                            .padding(15.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        AsyncImage(
                            model = taleCreation.image[page],
                            contentDescription = "Image",
                            error = painterResource(R.drawable.placeholder),
                            placeholder = painterResource(R.drawable.placeholder),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }*/
                    Box(Modifier.fillMaxWidth(0.9f)) {
                        Text(
                            text = taleCreation.story[page],
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
    }
}
