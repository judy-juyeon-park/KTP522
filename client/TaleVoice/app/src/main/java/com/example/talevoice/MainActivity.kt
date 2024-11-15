package com.example.talevoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.talevoice.ui.talelist.TaleListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tale_list)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TaleListFragment.newInstance())
                .commitNow()
        }
    }
}