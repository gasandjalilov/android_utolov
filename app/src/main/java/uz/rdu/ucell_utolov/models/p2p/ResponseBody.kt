package uz.rdu.ucell_utolov.models.p2p

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class ResponseBody(
    @SerializedName("response") val response: String?=null
):Parcelable,Serializable