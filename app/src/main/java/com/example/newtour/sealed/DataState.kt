package com.example.newtour.sealed

import com.example.newtour.models.Place

sealed class DataState{
    class Success(val data: MutableList<Place>):DataState()
    class Failure(val message: String):DataState()
    object Loading:DataState()
    object Empty:DataState()
}