package uz.rdu.ucell_utolov.interfaces.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.User

interface ApiTransactionPerform {

    @POST("login")
    fun login(@Body user: User): Call<AdvUser>


}