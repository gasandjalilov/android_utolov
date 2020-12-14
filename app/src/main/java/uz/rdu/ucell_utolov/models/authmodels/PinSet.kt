package uz.rdu.ucell_utolov.models.authmodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class PinSet (
    @SerializedName("pin") var pin: String? = null,
    @SerializedName("msisdn") var msisdn: String? = null
    ):Serializable, Parcelable