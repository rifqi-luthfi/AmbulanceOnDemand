
package com.example.ambulanceondemand.ui.landing.model

import com.google.gson.annotations.SerializedName


data class Bounds(
        @SerializedName("northeast")
        var northeast: Northeast?,
        @SerializedName("southwest")
        var southwest: Southwest?
)