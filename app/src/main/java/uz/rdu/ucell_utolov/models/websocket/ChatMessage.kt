package uz.rdu.ucell_utolov.models.websocket

import java.io.Serializable

data class ChatMessage(
    var from: String? = null,
    var to: String? = null,
    var message: String? = null
):Serializable