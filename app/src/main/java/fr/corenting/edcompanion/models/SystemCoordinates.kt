package fr.corenting.edcompanion.models

import com.google.gson.annotations.SerializedName

data class SystemCoordinates(@SerializedName("x") val x: Float,
                             @SerializedName("y") val y: Float,
                             @SerializedName("z") val z: Float)
