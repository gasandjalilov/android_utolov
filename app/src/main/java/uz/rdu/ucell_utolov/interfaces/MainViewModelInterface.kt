package uz.rdu.ucell_utolov.interfaces

import android.view.View
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.merchantmodels.Merchant
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse

interface MainViewModelInterface {

    fun getProfile()

    fun account(): AdvUser?

    fun myCardsButtonAction(v:View)

    fun transactionButtonAction(v:View)

    fun paymentButtonAction(v:View)

    fun upointsAction(v:View)
}