package com.fightpandemics.data.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.fightpandemics.result.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Storage for app and user preferences.
 */
interface FightPandemicsPreferenceDataStore {
    var userOnboardingFlow: Flow<Boolean>
    var onboardingCompleted: Boolean

    suspend fun saveToDataStore(onboard: Boolean)
}

private const val PREFERENCE_NAME = "fightpandemics"

/**
 * [PreferenceDataStore] impl backed by [androidx.datastore.DataStore].
 */
@Singleton
class FightPandemicsPreferenceDataStoreImpl @Inject constructor(context: Context) :
    FightPandemicsPreferenceDataStore {

    //Create some keys
    private object PreferenceKeys {
        val USER_ONBOARDING_KEY = preferencesKey<Boolean>("preference_onboarding")
    }

    // Create the Preferences DataStore
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    private val prefs: Lazy<SharedPreferences> = lazy { // Lazy to prevent IO access to main thread.
        context.applicationContext.getSharedPreferences(
            PREFERENCE_NAME, MODE_PRIVATE
        )
    }

    override var onboardingCompleted by BooleanPreference(prefs, "preference_onboarding", false)


    //
    override var userOnboardingFlow: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            it[PreferenceKeys.USER_ONBOARDING_KEY] ?: false
        }

    //
    override suspend fun saveToDataStore(onboard: Boolean) {
        dataStore.edit {
            it[PreferenceKeys.USER_ONBOARDING_KEY] = onboard
        }
    }
}

// Property Delegate
class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}