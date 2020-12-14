package uz.rdu.ucell_utolov.models.view

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import java.io.Serializable

@Parcelize
data class HomeFragmentModel(
    var amount: String?=null,
    var profileCards: List<ProfileResponse>?=null,
    var user: AdvUser?=null
):Serializable,Parcelable