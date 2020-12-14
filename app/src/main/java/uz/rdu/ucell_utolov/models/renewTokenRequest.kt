package uz.rdu.ucell_utolov.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class renewTokenRequest(
    @ColumnInfo(name = "refresh_token") @SerializedName("refresh_token") var refresh_token:String? = null,
    @ColumnInfo(name = "username") @SerializedName("username") var username:String? = null
): Serializable, Parcelable