package uz.rdu.ucell_utolov.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Role(
    @SerializedName("id") var id:UUID,
    @SerializedName("role") var role:String,
    @SerializedName("actions") var actions:List<Action>
): Parcelable