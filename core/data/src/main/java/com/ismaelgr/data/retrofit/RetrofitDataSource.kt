package com.ismaelgr.data.retrofit

import com.ismaelgr.data.retrofit.dao.Bonoloto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitDataSource {
    
    @GET("buscadorSorteos")
    suspend fun getBonolotos(
        @Query("game_id")
        game_id: String = "BONO",
        @Query("celebrados")
        celebrados: String = "true",
        @Query("fechaInicioInclusiva")
        fechaInicioInclusiva: String,
        @Query("fechaFinInclusiva")
        fechaFinInclusiva: String,
    ): Response<List<Bonoloto>>
}