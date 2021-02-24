package uz.rdu.ucell_utolov.models.ucell

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Data(
    var availableBalance: String? = null,
    var availableBalanceSrc: String? = null,
    var balanceId: String? = null,
    var balanceName: String? = null,
    var balanceType: String? = null,
    var expDate: String? = null,
    var maxLimit: String? = null,
    var totalBalance: String? = null,
    var totalBalanceSrc: String? = null
):Serializable, Parcelable