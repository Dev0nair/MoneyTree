package com.ismaelgr.data.retrofit.dao

import com.google.gson.annotations.SerializedName

data class Bonoloto(
    @SerializedName("fecha_sorteo")
    var fechaSorteo: String? = null,
    @SerializedName("dia_semana")
    var diaSemana: String? = null,
    @SerializedName("id_sorteo")
    var idSorteo: String? = null,
    @SerializedName("game_id")
    var gameId: String? = null,
    @SerializedName("anyo")
    var anyo: String? = null,
    @SerializedName("numero")
    var numero: Int? = null,
    @SerializedName("premio_bote")
    var premioBote: String? = null,
    @SerializedName("cdc")
    var cdc: String? = null,
    @SerializedName("apuestas")
    var apuestas: String? = null,
    @SerializedName("recaudacion")
    var recaudacion: String? = null,
    @SerializedName("combinacion")
    var combinacion: String? = null,
    @SerializedName("combinacion_acta")
    var combinacionActa: String? = null,
    @SerializedName("premios")
    var premios: String? = null,
    @SerializedName("fondo_bote")
    var fondoBote: String? = null,
    @SerializedName("escrutinio")
    var escrutinio: ArrayList<Escrutinio> = arrayListOf(),
    @SerializedName("contenidosRelacionados")
    var contenidosRelacionados: ContenidosRelacionados? = ContenidosRelacionados()
) {
    
    fun getSimpleBonoloto(): SimpleBonoloto {
        return SimpleBonoloto(getSimpleDate().orEmpty(),
            combinacion
                ?: ""
        )
    }
    
    fun getSimpleDate(): String? = (fechaSorteo)?.split(" ")?.get(0)?.replace("-", "")
}