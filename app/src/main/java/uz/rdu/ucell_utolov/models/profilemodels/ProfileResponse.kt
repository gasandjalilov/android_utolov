package uz.rdu.ucell_utolov.models.profilemodels

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity
@Parcelize
data class ProfileResponse (
    @Ignore @SerializedName("subscr_no") val subscr_no : String?=null,
    @Ignore @SerializedName("profile_created") val profile_created : String?=null,
    @Ignore @SerializedName("profile_modified") val profile_modified : String?=null,
    @Ignore @SerializedName("default_card") val default_card : Boolean?=null,
    @SerializedName("card_number") @PrimaryKey var card_number : String,
    @SerializedName("card_expire") @ColumnInfo(name = "card_expire")  var card_expire : String?=null,
    @SerializedName("card_state") @ColumnInfo(name = "card_state") var card_state : Boolean?=null,
    @Ignore @SerializedName("balance") var balance : String?=null,
    @ColumnInfo(name = "color") var card_color: Int?=null,
    @ColumnInfo(name = "card_name") var card_name: String?=null
    ):Serializable,Parcelable{
    constructor() : this(card_number="")
}