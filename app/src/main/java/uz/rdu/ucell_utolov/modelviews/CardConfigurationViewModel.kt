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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import kotlinx.coroutines.runBlocking
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.fragments.CardConfigurationFragmentDirections
import uz.rdu.ucell_utolov.fragments.RegistrationFragmentDirections
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.interfaces.api.ApiProfileInterface
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.profilemodels.CardAddRequest
import uz.rdu.ucell_utolov.models.profilemodels.CardDeleteRequest
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import javax.inject.Inject

class CardConfigurationViewModel(activity: Activity) : ViewModel() {

    lateinit var cardView: CardView
    lateinit var card: ProfileResponse
    lateinit var progressBar: ProgressBar
    lateinit var mainViewModel: MainViewModel
    var cardname = ObservableField<String>()
    var cardnumber = ObservableField<String>()
    var cardexp = ObservableField<String>()
    var user: AdvUser?
    var progress = MutableLiveData<Int>(View.GONE)
    var dialog: Dialog


    @Inject
    lateinit var applicationModule: ApiProfileInterface

    @Inject
    lateinit var context: Context

    init {
        MainApplication.component.inject(this)
        user = SharedPrefHelper(context).getUserObject()
        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_result)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


    }

    fun blackButton(v: View) {
        card.card_color = R.drawable.card_gradient_5
        cardView.setBackgroundResource(R.drawable.card_gradient_5)
    }

    fun yellowkButton(v: View) {
        card.card_color = R.drawable.card_gradient_3
        cardView.setBackgroundResource(R.drawable.card_gradient_3)
    }

    fun pinkButton(v: View) {
        card.card_color = R.drawable.card_gradient_4
        cardView.setBackgroundResource(R.drawable.card_gradient_4)
    }

    fun purpleButton(v: View) {
        card.card_color = R.drawable.card_gradient_1
        cardView.setBackgroundResource(R.drawable.card_gradient_1)
    }


    fun save(v: View) {
        runBlocking {
            progressBar.visibility = View.VISIBLE
        }
        var nonDigits = Regex("[^\\d]")
        var cardReq: CardAddRequest = CardAddRequest(
            cardexp.get().toString().replace(nonDigits, ""), cardnumber.get().toString().replace(
                nonDigits,
                ""
            ), user?.username!!
        )
        this.card.card_name = cardname.get()
        this.card.card_number = cardnumber.get().toString().replace(nonDigits, "")
        this.card.card_expire = cardexp.get().toString().replace(nonDigits, "")


        var request = applicationModule.addCard(cardReq).execute()

        if (request.code() == 200) {
            var db =
                ApplicationDatabase.getAppDataBase(context)
            db?.profileResponseDao()?.insertCard(card)
            var text = dialog.findViewById(R.id.dialog_text) as TextView
            text.text = context.getText(R.string.transfer_perform_success)
            dialog.show()
            var button = dialog.findViewById(R.id.dialog_button) as ImageButton
            button.setOnClickListener {
                mainViewModel.getProfile()
                var fragmentDirection =
                    CardConfigurationFragmentDirections.actionCardConfigurationFragmentToHomeFragment()
                v.findNavController().navigate(fragmentDirection)
                mainViewModel.getProfile()
                dialog.dismiss()
            }
        } else {
            Toast.makeText(v.context, "ERROR", Toast.LENGTH_SHORT).show()

        }
        runBlocking {
            progressBar.visibility = View.GONE
        }
    }

    fun delete(v: View) {
        progress.value = View.VISIBLE
        var nonDigits = Regex("[^\\d]")
        var cardReq: CardDeleteRequest = CardDeleteRequest(
            card.card_number, user?.username!!
        )

        var request = applicationModule.deleteCard(cardReq).execute()
        if (request.code() == 200) {
            progress.value = View.GONE
            var db =
                ApplicationDatabase.getAppDataBase(context)
            db?.profileResponseDao()?.deleteCard(card)
            var text = dialog.findViewById(R.id.dialog_text) as TextView
            text.text = context.getText(R.string.transfer_perform_success)
            dialog.show()
            var button = dialog.findViewById(R.id.dialog_button) as ImageButton
            button.setOnClickListener {
                mainViewModel.getProfile()
                var fragmentDirection =
                    CardConfigurationFragmentDirections.actionCardConfigurationFragmentToHomeFragment()
                v.findNavController().navigate(fragmentDirection)
                mainViewModel.getProfile()
                dialog.dismiss()
            }
        } else {
            Toast.makeText(v.context, "ERROR", Toast.LENGTH_SHORT).show()
        }
    }


    fun update(v: View) {
        Toast.makeText(v.context, "Success", Toast.LENGTH_SHORT).show()
        var db =
            ApplicationDatabase.getAppDataBase(context)
        db?.profileResponseDao()?.updateCard(card)
    }
}