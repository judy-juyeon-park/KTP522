package com.example.talevoice.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class UserInfoViewModel : ViewModel() {

    var name by mutableStateOf("")
    var gender by mutableStateOf("남성")

    // Callback for when name or gender changes
    var onChanged: ((String, String) -> Unit)? = null

    // Function to handle name input changes
    fun onNameChanged(newName: String) {
        if (name != newName) {
            name = newName
            notifyChanges()
        }
    }

    // Function to handle gender selection changes
    fun onGenderChanged(newGender: String) {
        if (gender != newGender) {
            gender = newGender
            notifyChanges()
        }
    }

    // Notify changes to the observer
    private fun notifyChanges() {
        onChanged?.invoke(name, gender)
    }
}