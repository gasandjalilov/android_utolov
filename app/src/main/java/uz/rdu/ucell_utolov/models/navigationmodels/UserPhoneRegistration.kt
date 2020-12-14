package uz.rdu.ucell_utolov.models.navigationmodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserPhoneRegistration(
    @SerializedName("username") var username: String,
    @SerializedName("pin") var pin: Int?
): Parcelable