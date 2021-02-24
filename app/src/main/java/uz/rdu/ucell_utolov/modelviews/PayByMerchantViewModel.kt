package uz.rdu.ucell_utolov.modelviews

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.github.islamkhsh.CardSliderViewPager
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.interfaces.api.ApiTransactionPerform
import uz.rdu.ucell_utolov.models.transaction.TransactionPerformRequest
import uz.rdu.ucell_utolov.models.transaction.innertransaction.DBTransaction
import java.lang.Exception
import javax.inject.Inject

class PayByMerchantViewModel: ViewModel(){

    lateinit var cardSliderViewPager: CardSliderViewPager
    var transactionPerformRequest:TransactionPerformRequest = TransactionPerformRequest()
    var amount:ObservableField<String> = ObservableField()
    var account:ObservableField<String> = ObservableField()
    lateinit var url: String;
    lateinit var viewContext:Context
    var db : ApplicationDatabase



    @Inject
    lateinit var applicationModule: ApiTransactionPerform

    @Inject
    lateinit var context: Context

    init {
        MainApplication.component.inject(this)
        db= ApplicationDatabase.getAppDataBase(context)!!
    }

    fun pay(view:View){
        var cardId = cardSliderViewPager.currentItem
        var tag = cardSliderViewPager.findViewWithTag<View>(cardId)
        var cardnumber = tag.findViewById<TextView>(R.id.card_element_hiden).text
        var card_exp = tag.findViewById<TextView>(R.id.card_element_exp).text
        var user = SharedPrefHelper(view.context).getUserObject()!!
        transactionPerformRequest.msisdn = user.username
        transactionPerformRequest.pin=SharedPrefHelper(view.context).getPin()
        transactionPerformRequest.cardnumber=cardnumber.toString()
        transactionPerformRequest.cardexp=card_exp.toString().replace("/","")
        transactionPerformRequest.amount=amount.get()!!.toInt()
        transactionPerformRequest.notif_lang="ru"
        transactionPerformRequest.payment_account=account.get()
        Log.i("PayByMerchantViewModel",transactionPerformRequest.toString())

        var dialog = Dialog(viewContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_result)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var button = dialog.findViewById(R.id.dialog_button) as ImageButton
        var saveBtn = dialog.findViewById(R.id.dialog_save_button) as ImageButton
        saveBtn.setOnClickListener {
            saveTransaction()
            dialog.dismiss()
        }
        button.setOnClickListener {
            dialog.dismiss()
        }

        try {
            var result = applicationModule.pay(transactionPerformRequest).execute()
            if(result.isSuccessful){
                var text = dialog.findViewById(R.id.dialog_text) as TextView
                text.text = context.getText(R.string.transfer_perform_success)
                dialog.show()
            }
            else{
                var text = dialog.findViewById(R.id.dialog_text) as TextView
                text.text = context.getText(R.string.transfer_perform_faulty)
                dialog.show()

            }
        }
        catch (e: Exception){
            var text = dialog.findViewById(R.id.dialog_text) as TextView
            text.text = context.getText(R.string.transfer_perform_faulty)
            dialog.show()
        }
    }


    fun saveTransaction(){
        var transaction: DBTransaction = DBTransaction()
        transaction.amount = amount.get()?.replace(",", "")?.toInt()
        transaction.account = account.get()
        transaction.merchant_id = transactionPerformRequest.merchant_id
        transaction.url= url
        Log.i("Transaction",transaction.toString())
        db.transactionDao().insertHistory(transaction)
    }

}