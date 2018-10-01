package com.calcifer.redditstop.models

import com.google.gson.annotations.SerializedName

data class Top(
        @SerializedName("kind") val kind: String,
        @SerializedName("data") val topData: TopData
)

data class TopData(
        @SerializedName("modHash") val modHash: String,
        @SerializedName("dist") val dist: Int,
        @SerializedName("children") val children: ArrayList<Post>,
        @SerializedName("after") val after: String,
        @SerializedName("before") val before: Any
)