package com.alice.rodexapp.response

import com.google.gson.annotations.SerializedName

data class StartInspectionResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("inspectionResult")
    val inspectionResult: InspectionResult
)

data class InspectionResult(
    @field:SerializedName("inspectionId")
    val inspectionId: String,

    @field:SerializedName("status")
    val status: String
)
