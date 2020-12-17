package uz.rdu.ucell_utolov.models.transaction

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class TransactionPerformRequest(
    @SerializedName("amount") val amount : Int,
    @SerializedName("cardexp") val cardexp : Int,
    @SerializedName("cardnumber") val cardnumber : String,
    @SerializedName("id") val id : String,
    @SerializedName("ins_date") val ins_date : String,
    @SerializedName("merchant_id") val merchant_id : String,
    @SerializedName("msisdn") val msisdn : String,
    @SerializedName("notif_lang") val notif_lang : String,
    @SerializedName("payment_account") val payment_account : String,
    @SerializedName("pin") val pin : Int
):Serializable,Parcelable