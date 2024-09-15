package com.ismaelgr.data.retrofit.dao

import com.google.gson.annotations.SerializedName

data class ContenidosRelacionados(
    @SerializedName("noticias")
    var noticias: ArrayList<Noticias> = arrayListOf(),
    @SerializedName("enlaces")
    var enlaces: ArrayList<String> = arrayListOf(),
    @SerializedName("puntosDeVenta")
    var puntosDeVenta: ArrayList<String> = arrayListOf(),
    @SerializedName("documentos")
    var documentos: ArrayList<Documentos> = arrayListOf(),
    @SerializedName("imagenes")
    var imagenes: ArrayList<String> = arrayListOf(),
    @SerializedName("preguntasFrecuentes")
    var preguntasFrecuentes: ArrayList<String> = arrayListOf(),
    @SerializedName("paginasLibres")
    var paginasLibres: ArrayList<String> = arrayListOf()
)