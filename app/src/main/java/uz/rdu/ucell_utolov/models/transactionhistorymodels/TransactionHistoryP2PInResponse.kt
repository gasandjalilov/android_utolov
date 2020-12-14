package uz.rdu.ucell_utolov.models.transactionhistorymodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class TransactionHistoryP2PInResponse (
    @SerializedName("rownum") val rownum : Int
):Serializable,Parcelable