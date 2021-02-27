package uz.rdu.ucell_utolov.interfaces.api

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Call
import retrofit2.http.GET
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import uz.rdu.ucell_utolov.helpers.AuthHelper
import uz.rdu.ucell_utolov.helpers.Constants
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.articlemodels.Article
import uz.rdu.ucell_utolov.models.renewTokenResponse
import java.util.*
import java.util.concurrent.TimeUnit

interface ApiArticleInterface {


    @GET("all")
    fun allNews(): Observable<List<Article>>

    companion object Factory {



        fun create(context: Context?): ApiArticleInterface {
            val client = OkHttpClient.Builder()
                .addInterceptor {
                    var objString = SharedPrefHelper(context!!).getUserObject()
                    var newData = AuthHelper.getToken(context)
                    var token = newData?.token
                    var req = it.request().newBuilder().addHeader(
                        "Authorization",
                        "Bearer_" + token!!
                    ).build()
                    return@addInterceptor it.proceed(req)
                }

                .addInterceptor {
                    val request: Request = it.request()
                    var response: Response = it.proceed(request)

                    var tryCount = 0
                    while (response.code()!=200 && tryCount < 3) {
                        Log.d("ApiP2PPerformInterface", "Request is not successful - $tryCount")
                        tryCount++
                        var user: renewTokenResponse? = AuthHelper.getToken(context!!)
                        var objString = SharedPrefHelper(context).getUserObject()
                        objString?.token=user?.token
                        SharedPrefHelper(context).saveUserObject(objString)
                        // retry the request
                        response = it.proceed(request)
                    }
                    return@addInterceptor response
                }

                .connectTimeout(2000, TimeUnit.SECONDS).readTimeout(
                    2000,
                    TimeUnit.SECONDS
                ).build()


            val retrofit = retrofit2.Retrofit.Builder()

                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().serializeNulls().setLenient().create()
                    )
                )
                .baseUrl(Constants.ARTICLE_URL)
                .client(client)
                .build()
            return retrofit.create(ApiArticleInterface::class.java)
        }
    }

}