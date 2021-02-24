package uz.rdu.ucell_utolov.models.merchantmodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Merchant (
    @SerializedName("id") val id : Int,
    @SerializedName("merchant_id") val merchant_id : String,
    @SerializedName("name") val name : String,
    @SerializedName("organization") val organization : String,
    @SerializedName("type") val type : Int,
    @SerializedName("account_order") val account_order : Int,
    @SerializedName("account_template") val account_template : String,
    @SerializedName("account_regexp") val account_regexp : String,
    @SerializedName("account_prefix") val account_prefix : String,
    @SerializedName("account_login_msg") val account_login_msg : String,
    @SerializedName("amount_min") val amount_min : Int,
    @SerializedName("amount_max") val amount_max : Int,
    @SerializedName("amount_balance") val amount_balance : String,
    @SerializedName("date_start") val date_start : String,
    @SerializedName("date_end") val date_end : String,
    @SerializedName("enabled") val enabled : String
):Serializable,Parcelable