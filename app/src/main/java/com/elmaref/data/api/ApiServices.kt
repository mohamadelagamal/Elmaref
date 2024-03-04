package com.elmaref.data.api

import com.elmaref.data.model.quran.api.Ayah
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("recitations/{recitation_id}/by_ayah/{ayah_key}")
    suspend fun getSpecificAya(
        @Path("recitation_id") recitationId: Int,
        @Path("ayah_key") ayahKey: String
    ): Ayah
}