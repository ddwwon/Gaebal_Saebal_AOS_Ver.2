package com.gaebalsaebal.gaebal_saebal_aos_ver2

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class BojClient {
    companion object {

        private const val BASE_URL = "http://203.255.3.246:7071/"
        fun getApi(): BOJServices = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BOJServices::class.java)
    }
}

data class Title (
    @SerializedName("result") val result: String
)

interface BOJServices {
    @GET("bjapi")
    fun getBOJNumber (
        @Query("number") number: Int
    ): Call<Title>
}