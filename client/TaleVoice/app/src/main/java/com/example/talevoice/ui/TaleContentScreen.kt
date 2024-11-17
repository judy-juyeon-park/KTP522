package com.example.talevoice.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.talevoice.data.TaleItem

@Composable
fun TaleContentScreen(taleItem: TaleItem, navController: NavHostController, modifier: Modifier){
    Log.d("TaleContentScreen", taleItem.toString())

}
