package uz.rdu.ucell_utolov.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Entity
@Parcelize
data class AdvUser (
    @SerializedName("id") @PrimaryKey var id: String,
    @ColumnInfo(name = "username") @SerializedName("username") var username:String? = null,
    @ColumnInfo(name = "password") @SerializedName("password") var password:String? = null,
    @ColumnInfo(name = "firstname") @SerializedName("firstname") var firstname: String? = null,
    @ColumnInfo(name = "lastname") @SerializedName("lastname") var lastname: String? = null,
    @ColumnInfo(name = "pin") @SerializedName("pin") var pin:Int? = null,
    @Ignore @SerializedName("ins_date") var ins_date: Date? = null,
    @Ignore @SerializedName("upd_date") var upd_date: Date? = null,
    @ColumnInfo(name = "status") @SerializedName("status") var status: String? = null,
    @ColumnInfo(name = "token") @SerializedName("token") var token: String? = null,
    @ColumnInfo(name = "refreshToken") @SerializedName("refreshToken") var refreshtoken: String? = null,
    @Ignore @SerializedName("roles") var roles:List<Role>? = null
): Serializable, Parcelable{
    constructor() : this("")
}