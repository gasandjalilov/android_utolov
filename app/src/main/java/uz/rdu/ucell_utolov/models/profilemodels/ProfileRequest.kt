package uz.rdu.ucell_utolov.models.profilemodels

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
data class ProfileRequest(
    @SerializedName("msisdn") var msisdn:String
) : Serializable, Parcelable
