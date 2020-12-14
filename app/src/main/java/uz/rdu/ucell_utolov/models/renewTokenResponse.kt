package uz.rdu.ucell_utolov.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class renewTokenResponse(
    @SerializedName("id") var id: String,
    @SerializedName("token") var token:String? = null,
    @SerializedName("response_message") var response_message:String? = null,
    @SerializedName("refresh_token") var refresh_token:String? = null,
    @SerializedName("username") var username:String? = null
): Serializable, Parcelable