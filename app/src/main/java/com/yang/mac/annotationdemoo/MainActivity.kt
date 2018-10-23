package com.yang.mac.annotationdemoo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yang.mac.annotations.InjectString

class MainActivity : AppCompatActivity() {

    @InjectString
    public lateinit var world : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
