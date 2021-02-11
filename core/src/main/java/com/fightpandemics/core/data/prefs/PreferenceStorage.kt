package com.fightpandemics.core.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/*
* created by Osaigbovo Odiase
* */

// Storage for app and user preferences.
interface PreferenceStorage {
    var onboardingCompleted: Boolean
    var token: String?
    var userId: String?
    var uuid: String?
}

// [PreferenceStorage] impl backed by [android.content.SharedPreferences].
class FightPandemicsPreferenceDataStore @Inject constructor(
    private val context: Context
) : PreferenceStorage {

    private val sharedPreferences: Lazy<SharedPreferences> =
        lazy { // Lazy to prevent IO access to main thread.
            context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

    override var onboardingCompleted by BooleanPreference(
        sharedPreferences,
        PREF_ONBOARDING,
        false
    )

    override var token: String? by StringPreference(
        sharedPreferences,
        PREF_AUTH_TOKEN,
        null
    )

    override var userId: String? by StringPreference(
        sharedPreferences,
        PREF_USER_ID,
        null
    )

    override var uuid: String? by StringPreference(
        sharedPreferences,
        PREF_UUID,
        null
    )

    companion object {
        const val PREFS_NAME = "fightpandemics"
        const val PREF_ONBOARDING = "pref_onboarding"
        const val PREF_AUTH_TOKEN = "pref_auth_token"
        const val PREF_USER_ID = "pref_user_id"
        const val PREF_UUID = "pref_uuid"
    }
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

class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return preferences.value.getString(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}
