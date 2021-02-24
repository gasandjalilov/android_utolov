package uz.rdu.ucell_utolov.models.transaction.innertransaction

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.time.temporal.TemporalAmount
import java.util.*

@Entity
@Parcelize
data class DBTransaction(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var url: String? = null,
    var merchant_id : String? = null,
    var amount: Int?= null,
    var account: String? = null
):Serializable,Parcelable
{
    constructor():this(null,
        "","",0,"")
}
