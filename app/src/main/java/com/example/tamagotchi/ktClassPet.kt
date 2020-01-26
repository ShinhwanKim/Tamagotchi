package com.example.tamagotchi

import android.net.Uri

class ktClassPet(
        var name: String,
        var hunger: Int= 50,
        var stamina: Int= 50,
        var health: Int = 50,
        var emotion: Int = 50,
        var gold: Int = 500,
        var growth: Int = 0,
        var figure: Uri? = null
) {
    public fun eatFood(price:Int,upHunger:Int){
        hunger = hunger + upHunger
    }
    public fun eatMedicine(price:Int,upHealth:Int){
        health = health + upHealth
    }
}