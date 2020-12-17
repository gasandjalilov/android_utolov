package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.andrognito.pinlockview.PinLockListener
import com.andrognito.pinlockview.PinLockView
import com.google.android.material.snackbar.Snackbar
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentPinBinding
import uz.rdu.ucell_utolov.helpers.EventObserver
import uz.rdu.ucell_utolov.interfaces.api.ApiAuthInterface
import uz.rdu.ucell_utolov.models.navigationmodels.UserPhoneRegistration
import uz.rdu.ucell_utolov.modelviews.PinViewModel
import uz.rdu.ucell_utolov.modelviews.RegistrationViewModel
import javax.inject.Inject

class PinFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by activityViewModels()
    private lateinit var navController: NavController
    lateinit var userPhoneRegistration: UserPhoneRegistration
    lateinit var pinViewModel: PinViewModel
    lateinit var pin:PinLockView
    @Inject
    lateinit var applicationModule: ApiAuthInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MainApplication.component.inject(this)
        createUserObject(registrationViewModel.number.get())
        pinViewModel = PinViewModel(userPhoneRegistration)
        var pinBinding: FragmentPinBinding = DataBindingUtil.inflate<FragmentPinBinding>(
            inflater,
            R.layout.fragment_pin,
            container,
            false
        )
        pinBinding.vmodel = pinViewModel
        pin = pinBinding.pinLockView
        var pindots = pinBinding.indicatorDots

        pin.attachIndicatorDots(pindots)
        pin.setPinLockListener(object :PinLockListener{
            override fun onComplete(pin: String?) {
                checkPin(pin)
            }

            override fun onEmpty() {
            }

            override fun onPinChange(pinLength: Int, intermediatePin: String?) {
            }

        })

        // Inflate the layout for this fragment
        return pinBinding.root
    }

    fun createUserObject(number: String?){
        var regex = Regex("[^0-9]")
        userPhoneRegistration = UserPhoneRegistration(regex.replace(number!!, ""), null)
        Log.i("LoginFragment", userPhoneRegistration.toString())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        registrationViewModel.navigateScreen.observe(requireActivity(), EventObserver {
            navController.navigate(it as Int)
        })
    }

    fun checkPin(pin: String?){
        Log.d("CheckPin",pin)
        userPhoneRegistration.pin = pin?.toInt()

        var response = applicationModule.pin_activate(userPhoneRegistration).execute().body()
        Log.d("CheckPin Response","Request"+userPhoneRegistration + " Response: "+response.toString())

        if(response?.code==1) {
            val action = PinFragmentDirections.actionPinFragmentToPhonePinFragment()
            view?.findNavController()?.navigate(action)
        }
        else{
            this.pin.resetPinLockView()
            Snackbar.make(requireView(),"Не верный PIN",Snackbar.LENGTH_SHORT).show()
        }
    }
}