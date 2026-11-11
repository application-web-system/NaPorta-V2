package com.example.naporta

import android.content.Intent
import android.os.Bundle

class MyOrdersActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        findViewById<android.view.View>(R.id.btnVoltarCardapio)?.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }
}
