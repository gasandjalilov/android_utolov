package uz.rdu.ucell_utolov.models.merchantmodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class MerchantData(
    @SerializedName("id") val id: String,
    @SerializedName("merchant") val merchant: Merchant,
    @SerializedName("url") val url: String,
    @SerializedName("merchantCategory") val merchantCategory: MerchantCategory
): Serializable, Parcelable