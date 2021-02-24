package uz.rdu.ucell_utolov.interfaces.api

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import uz.rdu.ucell_utolov.helpers.AuthHelper
import uz.rdu.ucell_utolov.helpers.Constants
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.models.ApiResponse
import uz.rdu.ucell_utolov.models.profilemodels.CardAddRequest
import uz.rdu.ucell_utolov.models.profilemodels.CardDeleteRequest
import uz.rdu.ucell_utolov.models.profilemodels.ProfileApiResponse
import uz.rdu.ucell_utolov.models.profilemodels.ProfileRequest
import uz.rdu.ucell_utolov.models.renewTokenResponse
import java.util.concurrent.TimeUnit

interface ApiProfileInterface {

    @POST("retrieve")
    fun retrieve(@Body request: ProfileRequest): Observable<ProfileApiResponse>

    @POST("addcard")
    fun addCard(@Body request: CardAddRequest): Call<ApiResponse>

    @POST("deletecard")
    fun deleteCard(@Body request: CardDeleteRequest): Call<ApiResponse>


    companion object Factory {


        fun create(context: Context?): ApiProfileInterface {
            val cacheSize = (10 * 1024 * 1024).toLong()

            val myCache = Cache(context?.cacheDir, cacheSize)


            val client = OkHttpClient.Builder()
                .addInterceptor {
                    var token = SharedPrefHelper(context!!).getUserObject()
                    var req = it.request().newBuilder().addHeader(
                        "Authorization",
                        "Bearer_" + token?.token
                    ).build()
                    return@addInterceptor it.proceed(req)
                }
                .addInterceptor {
                    val request: Request = it.request()

                    var response: Response = it.proceed(request)

                    var tryCount = 0
                    while (response.code() != 200 && tryCount < 3) {
                        Log.d("ApiProfileInterface", "Request is not successful - ${response.body().toString()}")
                        tryCount++
                        var user: renewTokenResponse? = AuthHelper.getToken(context!!)
                        var objString = SharedPrefHelper(context).getUserObject()
                        objString?.token = user?.token
                        SharedPrefHelper(context).saveUserObject(objString)
                        // retry the request
                        response = it.proceed(request)

                    }
                    if(tryCount==3){
                        var request = it.request()
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 1).build()
                        Log.d("ApiProfileInterface", "Request from cache")
                        response = it.proceed(request)
                    }
                    return@addInterceptor response
                }
                .connectTimeout(200, TimeUnit.SECONDS).readTimeout(
                    200,
                    TimeUnit.SECONDS
                )
                .cache(myCache)
                .build()


            val retrofit = retrofit2.Retrofit.Builder()

                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().serializeNulls().setLenient().create()
                    )
                )
                .baseUrl(Constants.PROFILE_URL)
                .client(client)
                .build()
            return retrofit.create(ApiProfileInterface::class.java)
        }
    }
}