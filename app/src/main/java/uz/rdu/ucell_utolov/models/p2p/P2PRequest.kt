package uz.rdu.ucell_utolov.models.p2p

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


@Parcelize
data class P2PRequest(
    @SerializedName("amount") val amount: Int?,
    @SerializedName("cardexp") val cardexp: String?,
    @SerializedName("cardnumber") val cardnumber: String?,
    @SerializedName("lang") val lang: String?,
    @SerializedName("msisdn") val msisdn: String?,
    @SerializedName("receiverscard") val receiverscard: String?
):Serializable,Parcelable