package com.codemave.mobicomp.ui.login

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveCredentials(username: String, password: String) {
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }

    fun getCredentials(): Pair<String, String> {
        return Pair(sharedPreferences.getString("username", "")!!,
            sharedPreferences.getString("password", "")!!)
    }
}
