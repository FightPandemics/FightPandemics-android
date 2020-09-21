package com.fightpandemics.dagger.module

import android.content.Context
import android.content.SharedPreferences
import com.fightpandemics.data.prefs.PreferenceStorage
import com.fightpandemics.data.prefs.SharedPreferenceStorage
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
    fun provideSharedPreferences(): SharedPreferences =
        context.applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)
}
