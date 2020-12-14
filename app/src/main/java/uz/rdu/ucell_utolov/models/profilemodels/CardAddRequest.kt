package uz.rdu.ucell_utolov.models.profilemodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class CardAddRequest(
    @SerializedName("cardexp") val cardexp : String,
    @SerializedName("cardnumber") val cardnumber : String,
    @SerializedName("msisdn") val msisdn : String
): Serializable,Parcelable