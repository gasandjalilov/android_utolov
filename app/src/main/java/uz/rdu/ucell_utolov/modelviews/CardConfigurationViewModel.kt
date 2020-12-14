package uz.rdu.ucell_utolov.modelviews

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import java.util.*

class CardConfigurationViewModel():ViewModel() {

    lateinit var cardView: CardView
    lateinit var card:ProfileResponse
    var cardname = ObservableField<String>()
    var cardnumber = ObservableField<String>()
    var cardexp = ObservableField<String>()


    fun blackButton(v:View){
        cardView.setBackgroundResource(R.drawable.card_gradient_5)
    }

    fun yellowkButton(v:View){
        cardView.setBackgroundResource(R.drawable.card_gradient_3)
    }

    fun pinkButton(v:View){
        cardView.setBackgroundResource(R.drawable.card_gradient_4)
    }

    fun purpleButton(v:View){
        cardView.setBackgroundResource(R.drawable.card_gradient_1)
    }
}