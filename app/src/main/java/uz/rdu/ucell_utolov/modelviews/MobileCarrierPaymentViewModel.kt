package uz.rdu.ucell_utolov.modelviews

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.github.islamkhsh.CardSliderViewPager
import kotlinx.android.synthetic.main.card_element.view.*
import kotlinx.coroutines.runBlocking
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.helpers.adapters.CardsActionAdapter
import uz.rdu.ucell_utolov.interfaces.api.ApiAuthInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiTransactionPerform
import uz.rdu.ucell_utolov.models.transaction.TransactionPerformRequest
import javax.inject.Inject

class MobileCarrierPaymentViewModel : ViewModel() {

    lateinit var cardSliderViewPager: CardSliderViewPager
    var number: ObservableField<String> = ObservableField()
    var amount: ObservableField<String> = ObservableField()

    @Inject
    lateinit var applicationModule: ApiTransactionPerform

    @Inject
    lateinit var context: Context


    init {
        MainApplication.component.inject(this)

    }

    fun makePayment(v: View) {
        var cardId = cardSliderViewPager.currentItem
        var tag = cardSliderViewPager.findViewWithTag<View>(cardId)
        var cardnumber = tag.findViewById<TextView>(R.id.card_element_hiden).text
        var card_exp = tag.findViewById<TextView>(R.id.card_element_exp).text


        var merchant_id = "59d2392fe9a6400a24b95fc8"



        var shared = SharedPrefHelper(context)
        var usr = shared.getUserObject()
        var transactionPerformRequest: TransactionPerformRequest = TransactionPerformRequest()
        runBlocking {
            transactionPerformRequest.amount = amount.get()!!.toInt()
            transactionPerformRequest.cardnumber = cardnumber.toString()
            transactionPerformRequest.cardexp = card_exp.toString()
            transactionPerformRequest.notif_lang = "ru"
            transactionPerformRequest.merchant_id = merchant_id
            transactionPerformRequest.msisdn = usr?.username.toString()
            transactionPerformRequest.pin = shared.getPin()
            transactionPerformRequest.payment_account = number.get().toString()
        }


        var response = applicationModule.pay(transactionPerformRequest).execute()
        if(response.code() == 200) Toast.makeText(context,response.body().toString(),Toast.LENGTH_LONG)
        Log.i("Payment Perform", response.toString())

    }

}