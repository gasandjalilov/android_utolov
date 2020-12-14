package uz.rdu.ucell_utolov.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import java.io.Serializable

data class ApiResponse(
    @SerializedName("id") val id: String,
    @SerializedName("code") val code: Int,
    @SerializedName("body") val body: Any?=null,
    @SerializedName("ins_date") val ins_date: String
):Serializable