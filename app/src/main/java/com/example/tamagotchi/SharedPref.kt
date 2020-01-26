package com.example.tamagotchi

import android.content.Context

class SharedPref {
    val FILE_MY_PET_DATA = "myPetData"
    val FILE_GAME_OPTION = "gameOption"

    val KEY_NAME = "name"
    val KEY_HUNGER = "hunger"
    val KEY_STAMINA = "stamina"
    val KEY_HEALTH = "health"
    val KEY_EMOTION = "emotion"
    val KEY_GOLD = "gold"
    val KEY_GROWTH = "growth"

    fun setString(context: Context, prefsFileName : String, key : String, value : String) {
        val prefs = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
        val editor = prefs!!.edit()
        editor.putString(key, value).apply()
    }
    fun getString(context: Context,prefsFileName : String,key : String) : String{
        val prefs = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
        return prefs.getString(key, "EMPTY")!!
    }
    fun setInt(context: Context,prefsFileName : String,key : String, value : Int) {
        val prefs = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
        val editor = prefs!!.edit()
        editor.putInt(key, value).apply()
    }

    fun getInt(context: Context,prefsFileName : String,key : String,default: Int) : Int{
        val prefs = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
        return prefs.getInt(key, default)
    }
}