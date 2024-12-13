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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import com.example.talevoice.R
import com.example.talevoice.TaleApplication
import com.example.talevoice.data.TaleItem
import com.example.talevoice.viewmodel.TaleDetailViewModel
import com.example.talevoice.viewmodel.TaleDetailViewModelFactory

@Composable
fun TaleContentTopBarActions(taleItem: TaleItem) {
    Log.d("TaleContentTopBarActions", "TaleContentTopBarActions called")

    var isLiked by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            isLiked = true
            Log.d("TaleContentTopBarActions", "Liked: $isLiked for ${taleItem.title}")
        },
        enabled = !isLiked
    ) {
        Icon(
            imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
            contentDescription = if (isLiked) "Unlike" else "Like"
        )
    }
}


@Composable
fun TaleContentScreen(taleItem: TaleItem) {
    Log.d("TaleContentScreen", taleItem.toString())

    val ttsApiService = (LocalContext.current.applicationContext as TaleApplication).ttsApiService
    val viewModel: TaleDetailViewModel = viewModel(
        factory = TaleDetailViewModelFactory(ttsApiService, taleItem)
    )

    val pageResults by viewModel.pageResults.collectAsState()

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    var isPlayingAudio by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(pageCount = {
        taleItem.context.size
    })


    LaunchedEffect(Unit) {
        Log.d("TaleContentScreen", "launchEffect on TaleContentScreen")
        viewModel.cleatData()
        viewModel.fetchSpeech(context.applicationContext)
    }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isPlayingAudio = isPlaying
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        exoPlayer.stop()
    }

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
                            model = if (taleItem.image.isNotEmpty() && page < taleItem.image.size) {
                                taleItem.image[page]
                            } else {
                                R.drawable.placeholder
                            },
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

        FloatingActionButton(
            onClick = {
                val currentPage = pagerState.currentPage
                val audioUri = pageResults[currentPage]
                if (audioUri != null) {
                    if (isPlayingAudio) {
                        exoPlayer.stop()
                        exoPlayer.clearMediaItems()
                    } else {
                        val mediaItem = MediaItem.fromUri(audioUri)
                        exoPlayer.setMediaItem(mediaItem)
                        exoPlayer.prepare()
                        exoPlayer.play()
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            val currentPage = pagerState.currentPage
            val audioData = pageResults[currentPage]
            if (audioData != null) {
                if (isPlayingAudio) {
                    val stopIcon = ImageVector.vectorResource(id = R.drawable.baseline_stop_24)
                    Icon(stopIcon, contentDescription = "Stop")
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                }

            } else {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }
}
