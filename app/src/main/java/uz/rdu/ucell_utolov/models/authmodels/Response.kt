package uz.rdu.ucell_utolov.models.authmodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Response(
    @SerializedName("message") var message: String? = null,
    @SerializedName("code") var code: Int? = null

    ):Serializable,Parcelable