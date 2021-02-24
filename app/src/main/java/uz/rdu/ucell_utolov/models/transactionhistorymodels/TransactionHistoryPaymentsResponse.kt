package uz.rdu.ucell_utolov.models.transactionhistorymodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class TransactionHistoryPaymentsResponse(
    @SerializedName("rownum") val rownum : Int,
    @SerializedName("account") val account : String,
    @SerializedName("amount") val amount : String,
    @SerializedName("merchant_id") val merchant_id : String,
    @SerializedName("merchant_name") val merchant_name: String,
    @SerializedName("pay_date") val pay_date : String
):Serializable,Parcelable