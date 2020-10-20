package com.fightpandemics.core.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// Storage for app and user preferences.
interface PreferenceStorage {
    var onboardingCompleted: Boolean
}

// [PreferenceStorage] impl backed by [android.content.SharedPreferences].
class FightPandemicsPreferenceDataStore @Inject constructor(
    private val context: Context, private val name: String,
) : PreferenceStorage {

    private val sharedPreferences: Lazy<SharedPreferences> =
        lazy { // Lazy to prevent IO access to main thread.
            context.applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)
        }

    override var onboardingCompleted by BooleanPreference(sharedPreferences,
        "preference_onboarding",
        false)
}

// Property Delegate
class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean,
) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        preferences.value.getBoolean(name, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
        preferences.value.edit { putBoolean(name, value) }
}