package com.fightpandemics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var info: Info

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}


class Info @Inject constructor() {
    val text = "Hello Dagger 2"
}

@Component
interface MagicBox {
    fun poke(app: MainActivity)
}
