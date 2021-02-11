package com.fightpandemics.core.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import java.util.*
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

    var uuid: String?

    var userId: String?
    var userFirstName: String?
    var userLastName: String?
    var userEmail: String?
    var userOrganisations: List<String>?
}

// [PreferenceStorage] impl backed by [android.content.SharedPreferences].
class FightPandemicsPreferenceDataStore @Inject constructor(
    private val context: Context
) : PreferenceStorage {

    companion object {
        const val PREFS_NAME = "fightpandemics"

        const val PREF_ONBOARDING = "pref_onboarding"

        const val PREF_AUTH_TOKEN = "pref_auth_token"

        const val PREF_UUID = "pref_uuid"

        const val PREF_USER_ID = "pref_user_id"
        const val PREF_USER_FIRST_NAME = "pref_user_first_name"
        const val PREF_USER_LAST_NAME = "pref_user_last_name"
        const val PREF_USER_EMAIL = "pref_user_email"
        const val PREF_USER_ORGANISATION = "pref_user_organisation"
    }

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

    override var uuid: String? by StringPreference(
        sharedPreferences,
        PREF_UUID,
        null
    )

    override var userId: String? by StringPreference(
        sharedPreferences,
        PREF_USER_ID,
        null
    )

    override var userFirstName: String? by StringPreference(
        sharedPreferences,
        PREF_USER_FIRST_NAME,
        null
    )

    override var userLastName: String? by StringPreference(
        sharedPreferences,
        PREF_USER_LAST_NAME,
        null
    )

    override var userEmail: String? by StringPreference(
        sharedPreferences,
        PREF_USER_EMAIL,
        null
    )

    override var userOrganisations: List<String>? by ListPreference(
        sharedPreferences,
        PREF_USER_ORGANISATION,
        null
    )
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

// Convert List to HashSet
class ListPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: HashSet<String>?
) : ReadWriteProperty<Any, List<String>?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): List<String>? {
        return preferences.value.getStringSet(name, defaultValue)?.toList()
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: List<String>?) {
        preferences.value.edit { putStringSet(name, value?.toHashSet()) }
    }
}
