package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentCardConfigurationBinding
import uz.rdu.ucell_utolov.helpers.CardExpTextWatcher
import uz.rdu.ucell_utolov.helpers.CardNumberTextWatcher
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.modelviews.CardActionViewModel
import uz.rdu.ucell_utolov.modelviews.CardConfigurationViewModel


class CardConfigurationFragment : Fragment() {

    private val args: CardConfigurationFragmentArgs by navArgs()
    lateinit var cardConfigurationViewModel: CardConfigurationViewModel
    lateinit var card_numer: TextView
    lateinit var card_exp: TextView
    lateinit var card_name: TextView

    lateinit var cardsBinding: FragmentCardConfigurationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var profileCard: ProfileResponse = args.cardObject


        cardConfigurationViewModel = CardConfigurationViewModel()
        cardConfigurationViewModel.card = profileCard
        cardsBinding = DataBindingUtil.inflate<FragmentCardConfigurationBinding>(
            inflater,
            R.layout.fragment_card_configuration,
            container,
            false
        )
        cardsBinding.lifecycleOwner = this

        if (profileCard.card_number.isNullOrEmpty()) {
            cardsBinding.cardConfigButtonBlockcard.visibility = View.GONE
            cardsBinding.cardConfigButtonDelete.visibility = View.GONE
            cardsBinding.cardConfigButtonMakecrdmain.visibility = View.GONE
            cardsBinding.cardConfigButtonCreate.visibility =View.VISIBLE
        }
        else{
            cardsBinding.cardConfigButtonBlockcard.visibility = View.VISIBLE
            cardsBinding.cardConfigButtonDelete.visibility = View.VISIBLE
            cardsBinding.cardConfigButtonMakecrdmain.visibility = View.VISIBLE
            cardsBinding.cardConfigButtonCreate.visibility =View.GONE
        }

        var card = cardsBinding.include.findViewById(R.id.card_element_cardview) as CardView
        this.card_numer = cardsBinding.include.findViewById(R.id.card_element_number) as TextView
        this.card_exp = cardsBinding.include.findViewById(R.id.card_element_exp) as TextView
        this.card_name = cardsBinding.include.findViewById(R.id.card_element_card_name) as TextView
        cardConfigurationViewModel.cardView = card

        cardsBinding.cardAddCardnumber.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val nonDigits = Regex("[^\\d]")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString() != current) {
                    val userInput = s.toString().replace(nonDigits, "")
                    if (userInput.length <= 16) {
                        current = userInput.chunked(4).joinToString(" ")
                        s.filters = arrayOfNulls<InputFilter>(0)
                    }
                    s.replace(0, s.length, current, 0, current.length)
                    setText()
                }
            }
        })

        cardsBinding.cardAddCardnumber.inputType = InputType.TYPE_CLASS_DATETIME
        cardsBinding.cardAddExp.addTextChangedListener(object : TextWatcher {

            private var current = ""

            private val nonDigits = Regex("[^\\d]")

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.toString() != current) {
                    val userInput = s.toString().replace(nonDigits, "")
                    if (userInput.length <= 4) {
                        current = userInput.chunked(2).joinToString("/")
                        s.filters = arrayOfNulls<InputFilter>(0)
                    }
                    s.replace(0, s.length, current, 0, current.length)
                    setText()
                }
            }
        })
        cardsBinding.cardAddExp.inputType = InputType.TYPE_CLASS_DATETIME

        cardsBinding.cardAddName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                setText()
            }

        })

        cardsBinding.vmodel = cardConfigurationViewModel


        //card.setBackgroundResource(R.drawable.card_gradient_1)


        return cardsBinding.root
    }


    fun setText() {
        this.card_numer.text = cardsBinding.cardAddCardnumber.text
        this.card_exp.text = cardsBinding.cardAddExp.text
        this.card_name.text = cardsBinding.cardAddName.text
    }


}