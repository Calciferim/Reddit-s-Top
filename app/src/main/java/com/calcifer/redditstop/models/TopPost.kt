package com.calcifer.redditstop.models

import com.google.gson.annotations.SerializedName

data class TopPost(
        @SerializedName("kind") val kind: String,
        @SerializedName("data") val topPostData: TopPostData
)