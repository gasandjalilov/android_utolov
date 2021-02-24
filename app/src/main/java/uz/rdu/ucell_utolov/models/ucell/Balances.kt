package uz.rdu.ucell_utolov.models.ucell

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Balances (
    var voiceBalance1: String? = null,
    var voiceBalance2: String? = null,
    var voiceBalance3: String? = null,
    var dataBalance1: String? = null,
    var dataBalance2: String? = null,
    var dataBalance3: String? = null,
    var dataBalance4: String? = null,
    var dataBalance5: String? = null
    ): Serializable, Parcelable