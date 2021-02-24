package uz.rdu.ucell_utolov.models.ucell

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class UcellResponse(
    var availBalance: String? = null,
    var data: List<Data>? = null,
    var msisdn: String? = null,
    var ucellSub: Int? = null,
    var primaryOffer: String? =null,
    var state: Int? = null
):Serializable, Parcelable