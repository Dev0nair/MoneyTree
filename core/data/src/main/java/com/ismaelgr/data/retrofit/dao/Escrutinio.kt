package com.ismaelgr.data.retrofit.dao

import com.google.gson.annotations.SerializedName

data class Escrutinio(
    @SerializedName("tipo")
    var tipo: String? = null,
    @SerializedName("categoria")
    var categoria: Int? = null,
    @SerializedName("premio")
    var premio: String? = null,
    @SerializedName("ganadores")
    var ganadores: String? = null
)