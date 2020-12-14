package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.sapereaude.maskedEditText.MaskedEditText
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentRegistrationBinding
import uz.rdu.ucell_utolov.helpers.EventObserver
import uz.rdu.ucell_utolov.interfaces.api.ApiAuthInterface
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.modelviews.RegistrationViewModel
import javax.inject.Inject

class RegistrationFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by activityViewModels()
    private lateinit var navController: NavController

    @Inject
    lateinit var applicationModule: ApiAuthInterface


    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        MainApplication.component.inject(this)

        //registrationViewModel = RegistrationViewModel()
        var registrationBinding: FragmentRegistrationBinding =
            DataBindingUtil.inflate<FragmentRegistrationBinding>(
                inflater,
                R.layout.fragment_registration,
                container,
                false
            )
        registrationBinding.vmodel = registrationViewModel

        var progressBar2: ProgressBar =
            registrationBinding.root.findViewById(R.id.progressBar2) as ProgressBar
        var editTextPhone: MaskedEditText =
            registrationBinding.root.findViewById(R.id.editTextPhone) as MaskedEditText

        var editTextPassword: EditText =
            registrationBinding.root.findViewById(R.id.registration_password) as EditText
        var editTextFirstanme: EditText =
            registrationBinding.root.findViewById(R.id.registration_firstname) as EditText
        var editTextLastname: EditText =
            registrationBinding.root.findViewById(R.id.registration_lastname) as EditText


        var registrationTextView: TextView =
            registrationBinding.root.findViewById(R.id.registration_info) as TextView
        var authButton = registrationBinding.root.findViewById(R.id.button_auth) as Button
        var registrationButton =
            registrationBinding.root.findViewById(R.id.button_registration) as Button

        editTextPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! == 20) {
                    var username = s.toString().replace("[^0-9]".toRegex(), "")
                    var user: AdvUser? = AdvUser()
                    progressBar2.visibility = View.VISIBLE
                    activity!!.runOnUiThread {
                        user = applicationModule.get_user_by_name(username).execute().body()
                        if (!user?.id.isNullOrBlank()) {
                            progressBar2.visibility = View.GONE

                            //
                            editTextPassword.visibility = View.VISIBLE
                            authButton.visibility = View.VISIBLE
                            //
                            editTextFirstanme.visibility = View.GONE
                            editTextLastname.visibility = View.GONE
                            registrationButton.visibility = View.GONE
                            //
                            registrationTextView.visibility = View.GONE

                        } else {
                            progressBar2.visibility = View.GONE

                            //
                            editTextPassword.visibility = View.VISIBLE
                            authButton.visibility = View.GONE
                            //
                            editTextFirstanme.visibility = View.VISIBLE
                            editTextLastname.visibility = View.VISIBLE
                            registrationButton.visibility = View.VISIBLE
                            //
                            registrationTextView.visibility = View.GONE

                        }
                    }
                } else {
                    progressBar2.visibility = View.GONE


                    editTextPassword.visibility = View.GONE
                    authButton.visibility = View.GONE
                    //
                    editTextPassword.visibility = View.GONE
                    authButton.visibility = View.GONE
                    //
                    editTextFirstanme.visibility = View.GONE
                    editTextLastname.visibility = View.GONE
                    registrationButton.visibility = View.GONE
                    //
                    registrationTextView.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        return registrationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        registrationViewModel.navigateScreen.observe(requireActivity(), EventObserver {
            navController.navigate(it as Int)
        })
    }
}

