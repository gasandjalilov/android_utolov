package uz.rdu.ucell_utolov.models.articlemodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
data class Article(
    var endDate: Date? = null,
    var id: String? = null,
    var status: Int? = null,
    var text: String? = null,
    var title: String? = null,
    var url: String? = null
):Serializable,Parcelable