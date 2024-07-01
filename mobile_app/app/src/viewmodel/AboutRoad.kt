package com.alice.rodexapp.viewmodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class AboutRoad(
    val name: String,
    val description: String,
    val photo: Int
) : Parcelable

