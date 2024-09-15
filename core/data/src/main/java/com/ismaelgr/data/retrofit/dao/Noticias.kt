package com.ismaelgr.data.retrofit.dao

import com.google.gson.annotations.SerializedName

data class Noticias(
    @SerializedName("tituloRelacion")
    var tituloRelacion: String? = null,
    @SerializedName("tituloContenido")
    var tituloContenido: String? = null,
    @SerializedName("urlContenido")
    var urlContenido: String? = null
)