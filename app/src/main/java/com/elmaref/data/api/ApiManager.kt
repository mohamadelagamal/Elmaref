package com.elmaref.data.api

import android.util.Log
import com.quranscreen.constants.LocaleConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//.. to build retrofit
class ApiManager {
    //... now we use Singleton pattern to use this object in all application
    companion object{
      private  var retrofit:Retrofit?=null
      private  fun getRetrofitInstance():Retrofit{
            if (retrofit==null){
                // this is a logging interceptor used to log the network calls
                val logging = HttpLoggingInterceptor { message -> Log.e("apiInterceptor", message) }
                // setLevel is used to set the level of logging
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                // this is a client used to make network calls
                val client = OkHttpClient.Builder()//
                    .addInterceptor(logging) // add the logging interceptor to the client
                    .build()// build the client
                // this is a retrofit instance used to make network calls

                 retrofit = Retrofit.Builder()
                    .baseUrl(LocaleConstants.AYAH_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
        fun getApi():ApiServices{
            return getRetrofitInstance().create(ApiServices::class.java)
        }
    }

}