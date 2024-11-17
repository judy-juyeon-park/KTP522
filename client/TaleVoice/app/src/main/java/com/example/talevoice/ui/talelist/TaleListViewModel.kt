package com.example.talevoice.ui.talelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.talevoice.TaleApplication
import com.example.talevoice.data.TaleItem
import com.example.talevoice.data.TaleListItem
import com.example.talevoice.data.TaleRepository

class TaleListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaleRepository =
        (application as TaleApplication).taleRepository

    val taleList: LiveData<List<TaleListItem>> = repository.getTaleList()

    suspend fun getTaleDetail(taleId: String) :TaleItem {
        return repository.getTaleItem(taleId)
    }
}