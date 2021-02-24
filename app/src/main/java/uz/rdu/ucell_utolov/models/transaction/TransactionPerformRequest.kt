package uz.rdu.ucell_utolov.models.transaction

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


@Parcelize
data class TransactionPerformRequest(
    @SerializedName("amount") var amount : Int?=null,
    @SerializedName("cardexp") var cardexp : String?=null,
    @SerializedName("cardnumber") var cardnumber : String?=null,
    @SerializedName("id") val id : String? =null,
    @SerializedName("ins_date") val ins_date : String? = null,
    @SerializedName("merchant_id") var merchant_id : String?=null,
    @SerializedName("msisdn") var msisdn : String?=null,
    @SerializedName("notif_lang") var notif_lang : String?=null,
    @SerializedName("payment_account") var payment_account : String?=null,
    @SerializedName("pin") var pin : String? = null
):Serializable,Parcelable