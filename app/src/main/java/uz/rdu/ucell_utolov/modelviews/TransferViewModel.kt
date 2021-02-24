package uz.rdu.ucell_utolov.modelviews

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.github.islamkhsh.CardSliderViewPager
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.interfaces.api.ApiP2PPerformInterface
import uz.rdu.ucell_utolov.models.p2p.P2PRequest
import uz.rdu.ucell_utolov.models.transaction.innertransaction.DBTransaction
import javax.inject.Inject


class TransferViewModel : ViewModel() {


    @Inject
    lateinit var applicationModule: ApiP2PPerformInterface

    @Inject
    lateinit var context: Context

    lateinit var viewContext:Context
    lateinit var cardSliderViewPager: CardSliderViewPager
    var amount: ObservableField<String> = ObservableField()
    var paymentcard: ObservableField<String> = ObservableField()
    var enabled:ObservableField<Boolean> = ObservableField()

    init {
        MainApplication.component.inject(this)
        enabled.set(true)
    }



    fun performTransfer(v: View){

        enabled.set(false)
        var cardId = cardSliderViewPager.currentItem
        var tag = cardSliderViewPager.findViewWithTag<View>(cardId)
        var cardnumber = tag.findViewById<TextView>(R.id.card_element_hiden).text
        var card_exp = tag.findViewById<TextView>(R.id.card_element_exp).text
        var msisdn = SharedPrefHelper(v.context).getUserObject()!!.username
        if(amount.get().isNullOrEmpty() or paymentcard.get().isNullOrEmpty()){
            var dialog = Dialog(viewContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_result)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            var button = dialog.findViewById(R.id.dialog_button) as ImageButton
            button.setOnClickListener {
                dialog.dismiss()
            }
            var text = dialog.findViewById(R.id.dialog_text) as TextView
            text.text = "No Data Presented"
            dialog.show()
        }
        else {


            var request: P2PRequest = P2PRequest(
                amount.get()?.replace(",", "")?.toInt(),
                cardexp = card_exp.toString().replace("/",""),
                msisdn = msisdn,
                lang = "ru",
                cardnumber = cardnumber.toString(),
                receiverscard = paymentcard.get()?.replace(
                    "\\s".toRegex(),
                    ""
                )
            )
            var dialog = Dialog(viewContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_result)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            var button = dialog.findViewById(R.id.dialog_button) as ImageButton
            button.setOnClickListener {
                dialog.dismiss()
            }
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            var requestExec = applicationModule.pay(request).execute()
            Log.i("Response",requestExec.body().toString())
            if (requestExec.isSuccessful && requestExec.body()!!.code==1) {
                var text = dialog.findViewById(R.id.dialog_text) as TextView
                text.text = context.getText(R.string.transfer_perform_success)
                dialog.show()
                enabled.set(true)
            } else {
                var text = dialog.findViewById(R.id.dialog_text) as TextView
                text.text = context.getText(R.string.transfer_perform_faulty)
                dialog.show()
                enabled.set(true)
            }
        }
        //Log.i("TransferViewModel: ",paymentcard.get()!!.replace("\\s".toRegex(), "") + " " +amount.get()?.replace(",", "") + " "+ cardnumber + " "+ card_exp)

    }



}