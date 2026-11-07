package com.example.naporta

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<android.view.View>(R.id.cardAdmin).setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnCarrinho).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        CartStore.loadPreviewDataIfEmpty()
    }
}