package com.example.webservice_example

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WSinterface {
    @GET("game/list")
    fun getAllToDos(): Call<List<GameList>>

    @GET("game/details")
    fun getGameDetail(@Query("game_id") game_id: Int): Call<GameDetail>
}