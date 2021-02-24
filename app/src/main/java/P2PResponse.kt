import uz.rdu.ucell_utolov.models.p2p.ResponseBody

data class P2PResponse(
    val body: ResponseBody,
    val code: Int,
    val id: String,
    val ins_date: String
)