package com.fightpandemics.data.prefs

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
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
    val userOnboardingFlow: Flow<Boolean>
    //var onboardingCompleted: Boolean
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
    suspend fun saveToDataStore(onboard: Boolean) {
        dataStore.edit {
            it[PreferenceKeys.USER_ONBOARDING_KEY] = onboard
        }
    }
}