package uz.rdu.ucell_utolov.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.andrognito.pinlockview.PinLockListener
import kotlinx.android.synthetic.main.fragment_pin_auth.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    lateinit var user: AdvUser
    lateinit var pinProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(context, "!!!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //pinProgress = requireView().findViewById(R.id.progressBarPinAuth)

        pinAuthViewModel = PinAuthViewModel()
        var authBinding: FragmentPinAuthBinding =
            DataBindingUtil.inflate<FragmentPinAuthBinding>(
                inflater,
                R.layout.fragment_pin_auth,
                container,
                false
            )
        pinProgress = authBinding.progressBarPinAuth
        authBinding.vmodel = pinAuthViewModel
        var prefuser = SharedPrefHelper(requireContext())
        runBlocking {
            user = prefuser.getUserObject()!!
            authBinding.pinLockView.setPinLockListener(object : PinLockListener {
                override fun onComplete(pin: String?) {
                    Log.d("Pin", pin + "   " + user.pin)
                    runBlocking {
                        if (prefuser.validatePin(pin!!)) {
                            pinProgress.visibility = View.VISIBLE
                            GlobalScope.launch {
                                var fragmentDirection =
                                    PinAuthFragmentDirections.actionPinAuthFragmentToMainFragment(
                                        user
                                    )
                                view!!.findNavController().navigate(fragmentDirection)
                            }
                            pinProgress.visibility = View.GONE

                        } else {
                            authBinding.PinText.text = resources.getText(R.string.pin_enter_wrong)
                            authBinding.pinLockView.resetPinLockView()
                            val vibrator =
                                context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            if (Build.VERSION.SDK_INT >= 26) {
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        200,
                                        VibrationEffect.EFFECT_HEAVY_CLICK
                                    )
                                )
                            } else {
                                vibrator.vibrate(200)
                            }

                        }
                    }

                }

                override fun onEmpty() {}
                override fun onPinChange(pinLength: Int, intermediatePin: String?) {}
            })
        }

        authBinding.pinLockView.attachIndicatorDots(authBinding.indicatorDots)
        callBiometrics()
        return authBinding.root
    }


    fun callBiometrics() {
        //pinProgress.visibility = View.VISIBLE
        val executor = ContextCompat.getMainExecutor(requireContext())
        var biopromt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    pinProgress.visibility = View.VISIBLE
                    GlobalScope.launch {
                        var fragmentDirection =
                            PinAuthFragmentDirections.actionPinAuthFragmentToMainFragment(user)
                        view!!.findNavController().navigate(fragmentDirection)
                    }
                    pinProgress.visibility = View.GONE

                }

            })
        biopromt.authenticate(BiometricAuthHelper.callBiometric())
    }
}