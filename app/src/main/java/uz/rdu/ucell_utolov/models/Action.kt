package uz.rdu.ucell_utolov.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Action(
    @SerializedName("id") var id: UUID,
    @SerializedName("action") var action:String
):Serializable