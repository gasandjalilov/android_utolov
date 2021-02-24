package uz.rdu.ucell_utolov.models.merchantmodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
data class MerchantCategory (
    @SerializedName("id") val id : UUID,
    @SerializedName("category") val category: String
): Serializable, Parcelable