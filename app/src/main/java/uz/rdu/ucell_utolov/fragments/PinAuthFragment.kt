package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.andrognito.pinlockview.PinLockListener
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentPhonePinBinding
import uz.rdu.ucell_utolov.databinding.FragmentPinAuthBinding
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.helpers.appauth.BiometricAuthHelper
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.modelviews.PhonePinViewModel
import uz.rdu.ucell_utolov.modelviews.PinAuthViewModel


class PinAuthFragment : Fragment() {



    lateinit var pinAuthViewModel: PinAuthViewModel
    private lateinit var navController: NavController
    lateinit var user:AdvUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Toast.makeText(context,"!!!",Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        pinAuthViewModel = PinAuthViewModel()
        var authBinding: FragmentPinAuthBinding =
            DataBindingUtil.inflate<FragmentPinAuthBinding>(
                inflater,
                R.layout.fragment_pin_auth,
                container,
                false
            )
        authBinding.vmodel = pinAuthViewModel
        var prefuser = SharedPrefHelper(requireContext())
        user = prefuser.getUserObject()!!
        authBinding.pinLockView.setPinLockListener(object : PinLockListener {
            override fun onComplete(pin: String?) {
                Log.d("Pin",pin + "   " +user?.pin)
                if(prefuser.validatePin(pin!!)){
                    var fragmentDirection = PinAuthFragmentDirections.actionPinAuthFragmentToMainFragment(user)
                    view!!.findNavController().navigate(fragmentDirection)
                }
                else{
                    authBinding.PinText.setText(resources.getText(R.string.pin_enter_wrong))
                }

            }
            override fun onEmpty() {}
            override fun onPinChange(pinLength: Int, intermediatePin: String?) {}
        })
        authBinding.pinLockView.attachIndicatorDots(authBinding.indicatorDots)
        callBiometrics()
        return authBinding.root
    }


    fun callBiometrics(){
        val executor = ContextCompat.getMainExecutor(requireContext())
        var biopromt = BiometricPrompt(this,executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                var fragmentDirection = PinAuthFragmentDirections.actionPinAuthFragmentToMainFragment(user)
                view!!.findNavController().navigate(fragmentDirection)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

        })
        biopromt.authenticate(BiometricAuthHelper.callBiometric())
    }
}