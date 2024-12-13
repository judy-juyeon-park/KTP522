package com.example.talevoice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.talevoice.data.TaleItem
import com.example.talevoice.ui.TaleContentScreen
import com.example.talevoice.ui.UserInfoScreen
import com.example.talevoice.ui.TaleListScreen
import kotlinx.serialization.Serializable


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Serializable
object TaleList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {

    val navController = rememberNavController()

    // 현재 경로에 따른 제목 결정
    val currentScreenTitle = remember { mutableStateOf("Tale List") }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val canNavigateBack = remember { mutableStateOf(false)}

    // 좋아요 상태 관리
    val isLiked = remember { mutableStateOf(false) }
    // 좋아요 버튼 표시 여부
    val showLikeButton = remember { mutableStateOf(false) }

    Log.d("MyAPp", "update!!!")
    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        currentScreenTitle.value,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = if (currentScreenTitle.value.length > 10) 30.sp else 45.sp,
                    )
                },
                navigationIcon = {
                    if (canNavigateBack.value){
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                },
                actions = {
                    if (showLikeButton.value) {
                        IconButton(
                            onClick = {
                                isLiked.value = true
                                Log.d("MyApp", "Liked: ${isLiked.value}")
                            },
                            enabled = !isLiked.value
                        ) {
                            Icon(
                                imageVector = if (isLiked.value) {
                                    Icons.Filled.ThumbUp
                                } else {
                                    Icons.Outlined.ThumbUp
                                },
                                contentDescription = if (isLiked.value) "Unlike" else "Like"
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "UserInfoScreen",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("UserInfoScreen") {
                    canNavigateBack.value = false
                    showLikeButton.value = false // 좋아요 버튼 숨기기
                    currentScreenTitle.value = "TaleVoice" // 화면에 따른 제목
                    UserInfoScreen(navController)
                }
                composable("TaleList/{name}/{gender}") { backStackEntry ->
                    canNavigateBack.value = true
                    showLikeButton.value = false // 좋아요 버튼 숨기기
                    isLiked.value = false // 좋아요 버튼 초기화
                    val name = backStackEntry.arguments?.getString("name")
                    val gender = backStackEntry.arguments?.getString("gender")
                    currentScreenTitle.value = "동화 리스트" // 화면에 따른 제목
                    TaleListScreen(navController, name, gender)
                }
                composable<TaleItem> { backStackEntry ->
                    canNavigateBack.value = true
                    showLikeButton.value = true // 좋아요 버튼 표
                    val taleItem: TaleItem = backStackEntry.toRoute()
                    currentScreenTitle.value = taleItem.title // 화면에 따른 제목
                    TaleContentScreen(taleItem)
                }
            }
        }
    )
}