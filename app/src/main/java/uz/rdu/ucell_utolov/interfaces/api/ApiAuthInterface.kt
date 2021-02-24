package uz.rdu.ucell_utolov.interfaces.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uz.rdu.ucell_utolov.helpers.Constants
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.User
import uz.rdu.ucell_utolov.models.authmodels.PinSet
import uz.rdu.ucell_utolov.models.authmodels.Response
import uz.rdu.ucell_utolov.models.navigationmodels.UserPhoneRegistration
import java.util.concurrent.TimeUnit

interface ApiAuthInterface {

    @POST("login")
    fun login(@Body user:User): Call<AdvUser>

    @POST("sign_up")
    fun sign_up(@Body user: AdvUser): Call<AdvUser>

    @POST("renew")
    fun renew()

    @GET("get_user_by_name/{username}")
    fun get_user_by_name(@Path("username") username: String?): Call<AdvUser>


    @POST("pin_activate")
    fun pin_activate(@Body sms: UserPhoneRegistration): Call<Response>

    @POST("pin_set")
    fun pin_set(@Body pin_set:PinSet):Call<Response>

    companion object Factory{
        fun create(): ApiAuthInterface {
            val client = OkHttpClient
                .Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build()


            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().setLenient().create()))
                .baseUrl(Constants.AUTH_URL)
                .client(client)
                .build()
            return retrofit.create(ApiAuthInterface::class.java)
        }



    }
}