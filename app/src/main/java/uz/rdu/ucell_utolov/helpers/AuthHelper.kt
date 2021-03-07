package uz.rdu.ucell_utolov.helpers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.json.JSONObject
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.helpers.Constants.BASE_URL
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.User
import uz.rdu.ucell_utolov.models.renewTokenRequest
import uz.rdu.ucell_utolov.models.renewTokenResponse
import java.util.concurrent.TimeUnit

object AuthHelper {


    fun getToken(context: Context): renewTokenResponse? {
        var tmpuser: AdvUser? = null
        runBlocking {
            tmpuser = SharedPrefHelper(context).getUserObject()
        }

        val refreshRequest: renewTokenRequest =
            renewTokenRequest(refresh_token = tmpuser?.refreshtoken, username = tmpuser?.username)
        val str: String = Gson().toJson(refreshRequest)
        val requestBody = RequestBody.create(MediaType.parse("application/json"), str)
        val client = OkHttpClient.Builder().connectTimeout(200, TimeUnit.SECONDS).readTimeout(
            200,
            TimeUnit.SECONDS
        ).build()
        val req = Request
            .Builder()
            .url(BASE_URL + "/v1/auth/renew")
            .post(requestBody)
            .build()
        try {
            val resp = client.newCall(req).execute()
            if (resp.code() != 200) {
                return null
            } else {
                val some = JSONObject(resp.body()?.string() as String)
                val gson = GsonBuilder().create()
                val user: renewTokenResponse =
                    gson.fromJson(some.toString(), renewTokenResponse::class.java)
                return user
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
}