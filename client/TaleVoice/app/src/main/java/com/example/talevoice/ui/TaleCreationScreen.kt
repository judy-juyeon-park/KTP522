package com.example.talevoice.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.talevoice.TaleApplication
import com.example.talevoice.data.TaleCreation
import com.example.talevoice.viewmodel.TaleCreationViewModel
import com.example.talevoice.viewmodel.TaleCreationViewModelFactory

@Composable
//fun TaleCreationScreen(navController: NavHostController, title : String?) {
fun TaleCreationScreen(taleCreation: TaleCreation) {

    Log.d("TaleCreationScreen", "TaleCreationScreen")
    Log.d("TaleCreationScreen", "title: ${taleCreation.title}")
    Log.d("TaleCreationScreen", "story: ${taleCreation.story}")
    val repository = (LocalContext.current.applicationContext as TaleApplication).taleRepository
    val creationViewModel: TaleCreationViewModel = viewModel(
        factory = TaleCreationViewModelFactory(repository)
    )
    val createdTale by creationViewModel.createdTale.collectAsState()
    Log.d("TaleCreationScreen", "Created tale observed: $createdTale")


    // 화면 초기화
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        if (createdTale == null) {
            // 동화가 아직 생성 중인 경우 로딩 화면 표시
            Log.d("TaleCreationScreen", "loading")
            CircularProgressIndicator()
        } else {
            Log.d("TaleCreationScreen", "start")
            // 생성된 동화를 화면에 표시
            //val taleCreation = creationViewModel.getCreatedTaleItem()
            //Log.d("TaleCreationScreen", "title=${taleCreation?.title}" )
            //Log.d("TaleCreationScreen", "story=${taleCreation?.story}" )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = taleCreation!!.title,
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                taleCreation.story.forEach { paragraph ->
                    Text(
                        text = paragraph,
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}
