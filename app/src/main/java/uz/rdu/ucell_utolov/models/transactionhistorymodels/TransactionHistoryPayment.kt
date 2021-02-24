package uz.rdu.ucell_utolov.models.transactionhistorymodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import uz.rdu.ucell_utolov.models.p2p.ResponseBody
import java.io.Serializable

@Parcelize
data class TransactionHistoryPayment (
    @SerializedName("body") val body: List<TransactionHistoryPaymentsResponse>?=null,
    @SerializedName("code") val code: Int?=null,
    @SerializedName("id") val id: String?=null
): Serializable, Parcelable