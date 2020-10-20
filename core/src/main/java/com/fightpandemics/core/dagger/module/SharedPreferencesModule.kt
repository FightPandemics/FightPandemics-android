package com.fightpandemics.core.dagger.module

import android.content.Context
import android.content.SharedPreferences
import com.fightpandemics.core.data.prefs.FightPandemicsPreferenceDataStore
import com.fightpandemics.core.data.prefs.PreferenceStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provide [SharedPreferences] to this app's components.
 */
@Module
class SharedPreferencesModule(val context: Context, val name: String) {

    @Singleton
    @Provides
    fun provideString(): String = name

    @Singleton
    @Provides
    fun providesPreferenceStorage(
        context: Context, name: String
    ): PreferenceStorage =
        FightPandemicsPreferenceDataStore(context, name)
}
