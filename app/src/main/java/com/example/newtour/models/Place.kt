package com.example.newtour.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    var Image: String?=null,
    var Name: String?=null,
    var PlaceDescription: String?=null,
    var NearestFlight: String?=null,
    var Package: String?=null,
    var ToVisit: String?=null
): Parcelable