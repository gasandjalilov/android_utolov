package uz.rdu.ucell_utolov.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("username") var username: String? = null,
    @SerializedName("password") var password: String? = null
)