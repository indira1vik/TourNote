package com.example.newtour.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.newtour.models.Place
import com.example.newtour.sealed.DataState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainView: ViewModel() {
    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)

    init{
        fetchDataFromFireBase()
    }

    private fun fetchDataFromFireBase() {
        val tempList = mutableListOf<Place>()
        response.value = DataState.Loading

        FirebaseDatabase
            .getInstance()
            .getReference("myData/Category")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(EachData in snapshot.children){
                        val placeItem = EachData.getValue(Place::class.java)
                        if (placeItem != null){
                            tempList.add(placeItem)
                        }
                    }
                    response.value = DataState.Success(tempList)
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = DataState.Failure(error.message)
                }

            })
    }
}