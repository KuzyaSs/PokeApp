package com.example.pokeapp

import android.app.Application
import com.example.pokeapp.di.ApplicationComponent
import com.example.pokeapp.di.DaggerApplicationComponent

class PokeApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.create()
    }
}