package uz.rdu.ucell_utolov.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import com.andrognito.pinlockview.PinLockView
import com.goodiebag.pinview.Pinview
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_transfer.view.*
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentPhonePinBinding
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.interfaces.api.ApiAuthInterface
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.User
import uz.rdu.ucell_utolov.models.authmodels.PinSet
import uz.rdu.ucell_utolov.modelviews.PhonePinViewModel
import uz.rdu.ucell_utolov.modelviews.RegistrationViewModel
import javax.inject.Inject


class PhonePinFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    lateinit var phonePinViewModel: PhonePinViewModel
    private lateinit var navController: NavController
    lateinit var db:ApplicationDatabase

    @Inject
    lateinit var applicationModule: ApiAuthInterface


    lateinit var pin: String

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var prefuser = SharedPrefHelper(requireContext())
        MainApplication.component.inject(this)
        var bool: Boolean = false
        phonePinViewModel = PhonePinViewModel()
        var loginBinding: FragmentPhonePinBinding =
            DataBindingUtil.inflate<FragmentPhonePinBinding>(
                inflater,
                R.layout.fragment_phone_pin,
                container,
                false
            )
        loginBinding.vmodel = phonePinViewModel

        var validating_pin:Boolean = false
        lateinit var pin_value: String
        var text: TextView = loginBinding.PinText
        var pin: PinLockView = loginBinding.pinLockView
        var pin_dots: IndicatorDots = loginBinding.indicatorDots

        pin.setPinLockListener(object : PinLockListener{
            override fun onComplete(pin: String?) {
                if(!validating_pin){
                    pin_value=pin!!
                    validating_pin=true
                    text.text = resources.getString(R.string.pin_enter_pin_again)
                }
                else if(validating_pin){
                    if(pin_value==pin){
                        db = ApplicationDatabase.getAppDataBase(requireContext())!!
                        Log.d("PIN",registrationViewModel.number.get())
                        var regex = Regex("[^0-9]")
                        var username = regex.replace(registrationViewModel.number.get()!!,"")
                        var response = applicationModule.pin_set(PinSet(pin,username)).execute().body()
                        prefuser.savePin(pin)

                        Log.d("PIN CHANGED",response.toString())
                        var user = User(username,registrationViewModel.password.get())

                        var adbUser = applicationModule.login(user).execute().body()

                        prefuser.setUser(user)
                        prefuser.saveUserObject(adbUser)
                        Log.d("User",adbUser.toString())

                        var fragmentDirection = PhonePinFragmentDirections.actionPhonePinFragmentToMainFragment(adbUser!!)
                        view!!.findNavController().navigate(fragmentDirection)
                    }
                    else{
                        text.setTextColor(R.color.colorPrimary)
                        text.text=resources.getString(R.string.pin_enter_pin_again_wrong)
                    }
                }

            }

            override fun onEmpty() {
            }

            override fun onPinChange(pinLength: Int, intermediatePin: String?) {
            }

        })
        pin.attachIndicatorDots(pin_dots)

        return loginBinding.root

    }





}

