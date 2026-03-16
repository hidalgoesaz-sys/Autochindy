package com.example.autochindy.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PinManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "autochindy_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun hasPin(): Boolean {
        return sharedPrefs.contains("user_pin")
    }

    fun savePin(pin: String) {
        sharedPrefs.edit().putString("user_pin", pin).apply()
    }

    fun validatePin(pin: String): Boolean {
        val stored = sharedPrefs.getString("user_pin", null)
        return stored == pin
    }
}
