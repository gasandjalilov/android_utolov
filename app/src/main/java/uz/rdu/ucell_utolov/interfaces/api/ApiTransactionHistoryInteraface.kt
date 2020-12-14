package uz.rdu.ucell_utolov.interfaces.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import uz.rdu.ucell_utolov.helpers.Constants
import java.util.concurrent.TimeUnit

interface ApiTransactionHistoryInteraface {


    @POST("retrieveLastInP2P")
    fun retrieveLastInP2P()

    companion object Factory{
        fun create(): ApiTransactionHistoryInteraface {
            val client = OkHttpClient.Builder().connectTimeout(200, TimeUnit.SECONDS).readTimeout(200,
                TimeUnit.SECONDS).build()


            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().setLenient().create()))
                .baseUrl(Constants.TRANSACTION_HOSTORY_URL)
                .client(client)
                .build()
            return retrofit.create(ApiTransactionHistoryInteraface::class.java)
        }
    }
}