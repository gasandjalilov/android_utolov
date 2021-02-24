package uz.rdu.ucell_utolov.interfaces.api

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uz.rdu.ucell_utolov.helpers.AuthHelper
import uz.rdu.ucell_utolov.helpers.Constants
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.models.ApiResponse
import uz.rdu.ucell_utolov.models.merchantmodels.Merchant
import uz.rdu.ucell_utolov.models.merchantmodels.MerchantData
import uz.rdu.ucell_utolov.models.renewTokenResponse
import uz.rdu.ucell_utolov.models.transaction.TransactionPerformRequest
import java.util.concurrent.TimeUnit

interface ApiMerchantInterface {

    @GET("all")
    fun all(): Observable<List<Merchant>>

    @GET("get_all_merchant_data")
    fun allMerchantsWithImg(): Observable<List<MerchantData>>

    companion object Factory {



        fun create(context: Context?): ApiMerchantInterface {
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
                        Log.d("ApiMerchantInterface", "Request is not successful - $tryCount")
                        tryCount++
                        var user: renewTokenResponse? = AuthHelper.getToken(context!!)
                        var objString = SharedPrefHelper(context).getUserObject()
                        objString?.token = user?.token

                        SharedPrefHelper(context).saveUserObject(objString)
                        // retry the request
                        response = it.proceed(request)
                    }

                    // otherwise just pass the original response on

                    // otherwise just pass the original response on
                    return@addInterceptor response
                }
                .connectTimeout(200, TimeUnit.SECONDS).readTimeout(
                    200,
                    TimeUnit.SECONDS
                ).build()


            val retrofit = retrofit2.Retrofit.Builder()

                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().serializeNulls().setLenient().create()
                    )
                )
                .baseUrl(Constants.MERCHANT_URL)
                .client(client)
                .build()
            return retrofit.create(ApiMerchantInterface::class.java)
        }
    }
}