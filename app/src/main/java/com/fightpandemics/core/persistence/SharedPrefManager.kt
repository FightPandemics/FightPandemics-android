package com.fightpandemics.core.persistence

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.IOException
import java.security.GeneralSecurityException

/**
 * Created by aamirahmed
 */
class SharedPrefsManager(context: Context?) {
    var mSharedPreferences: SharedPreferences? = null

    //    protected void injectDependencies(Application application) {
    //
    //        DaggerApplicationComponent.builder()
    //                .applicationModule(new ApplicationModule(application))
    //                .build()
    //                .inject(this);
    //
    //    }
    fun getString(key: String?): String? {
        return mSharedPreferences!!.getString(key, "")
    }

    fun hasKey(key: String?): Boolean {
        return mSharedPreferences!!.contains(key)
    }

    fun getBooleanValue(key: String?): Boolean {
        return mSharedPreferences!!.getBoolean(key, false)
    }

    fun getBooleanValue(key: String?, defaultValue: Boolean): Boolean {
        return mSharedPreferences!!.getBoolean(key, defaultValue)
    }

    fun getString(key: String?, defaultValue: String): String {
        return (if (getString(key)!!.length == 0) defaultValue else getString(key))!!
    }

    fun getIntValue(key: String?): Int {
        return mSharedPreferences!!.getInt(key, 0)
    }

    fun getIntValue(key: String?, defaultValue: Int): Int {
        return if (getIntValue(key) == 0) defaultValue else getIntValue(key)
    }

    fun getLongValue(key: String?): Long {
        return mSharedPreferences!!.getLong(key, 0)
    }

    fun getLongValue(key: String?, defaultValue: Long): Long {
        return if (getLongValue(key) == 0L) defaultValue else getLongValue(key)
    }

    fun getFloatValue(key: String?): Float {
        return mSharedPreferences!!.getFloat(key, 0f)
    }

    fun getFloatValue(key: String?, defaultValue: Float): Float {
        return if (getFloatValue(key) == 0f) defaultValue else getFloatValue(key)
    }

    fun putStringValue(key: String?, value: String?) {
        mSharedPreferences!!.edit().putString(key, value).apply()
    }

    fun putIntValue(key: String?, value: Int) {
        mSharedPreferences!!.edit().putInt(key, value).apply()
    }

    fun putBooleanValue(key: String?, value: Boolean) {
        mSharedPreferences!!.edit().putBoolean(key, value).apply()
    }

    fun putLongValue(key: String?, value: Long) {
        mSharedPreferences!!.edit().putLong(key, value).apply()
    }

    fun putFloatValue(key: String?, value: Float) {
        mSharedPreferences!!.edit().putFloat(key, value).apply()
    }

    fun remove(key: String?) {
        mSharedPreferences!!.edit().remove(key).apply()
    }

    fun reset() {
        mSharedPreferences!!.edit().clear().commit()
    }

    init {
        try {
            //Todo : Can replace the deprecated MasterKeys implementation (with MasterKey.Builder) before pushing to Prod
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            mSharedPreferences = EncryptedSharedPreferences.create(
                "fp_prefs",
                masterKeyAlias,
                context!!,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}