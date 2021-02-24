package uz.rdu.ucell_utolov.models.p2p

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class P2PResponse(
    @SerializedName("body") val body: ResponseBody?=null,
    @SerializedName("code") val code: Int?=null,
    @SerializedName("id") val id: String?=null,
    @SerializedName("ins_date") val ins_date: String?=null
):Parcelable,Serializable