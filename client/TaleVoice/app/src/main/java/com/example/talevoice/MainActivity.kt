package com.example.talevoice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.talevoice.ui.TaleCreationScreen
import com.example.talevoice.ui.TaleInputScreen
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
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "TaleInputScreen",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("TaleInputScreen") {
                    canNavigateBack.value = false
                    currentScreenTitle.value = "TaleVoice" // 화면에 따른 제목
                    TaleInputScreen(navController)
                }
                /* composable<TaleList> {
                    canNavigateBack.value = true
                    currentScreenTitle.value = "동화 리스트" // 화면에 따른 제목
                    TaleListScreen(navController)
                }*/
                composable("TaleList/{name}/{gender}") { backStackEntry ->
                    canNavigateBack.value = true
                    val name = backStackEntry.arguments?.getString("name")
                    val gender = backStackEntry.arguments?.getString("gender")
                    currentScreenTitle.value = "동화 리스트" // 화면에 따른 제목
                    TaleListScreen(navController, name, gender)
                }
                composable<TaleItem> { backStackEntry ->
                    canNavigateBack.value = true
                    val taleItem: TaleItem = backStackEntry.toRoute()
                    currentScreenTitle.value = taleItem.title // 화면에 따른 제목
                    TaleContentScreen(taleItem)
                }
                composable("TaleCreationScreen") {
                    canNavigateBack.value = true
                    currentScreenTitle.value = "새로운 동화 생성"
                    TaleCreationScreen(navController)
                }
            }
        }
    )
}