package com.example.mapsapp.components

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleManager {
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    fun applyLocale(context: Context): Context {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val language = sharedPref.getString("language", "en") ?: "en"
        return setLocale(context, language)
    }
}