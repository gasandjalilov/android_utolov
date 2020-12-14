package uz.rdu.ucell_utolov.models.balancemodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class CardBalanceResponse(
    @SerializedName("balance") var balance:String?=null
):Serializable,Parcelable