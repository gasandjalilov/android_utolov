package uz.rdu.ucell_utolov.modelviews

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.*
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.dagger_implementations.component.DaggerApplicationComponent
import uz.rdu.ucell_utolov.fragments.RegistrationFragmentDirections
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.helpers.Event
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.interfaces.api.ApiAuthInterface
import uz.rdu.ucell_utolov.interfaces.RegistrationInterface
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.User
import uz.rdu.ucell_utolov.models.navigationmodels.UserPhoneRegistration
import java.lang.Exception
import javax.inject.Inject

class RegistrationViewModel:RegistrationInterface, ViewModel() {
    var isAnimation = ObservableBoolean(true)
    private val _navigateScreen = MutableLiveData<Event<Any>>()
    val navigateScreen: LiveData<Event<Any>> = _navigateScreen
    var number: ObservableField<String> = ObservableField<String>()

    var username: ObservableField<String> = ObservableField<String>()
    var password: ObservableField<String> = ObservableField<String>()
    var user: AdvUser = AdvUser()

    @Inject
    lateinit var applicationModule: ApiAuthInterface

    @Inject
    lateinit var context: Context

    fun init(){
        MainApplication.component.inject(this)
    }

    override fun login(v :View) {
        //val action = RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
        //v.findNavController().navigate(action)
        MainApplication.component.inject(this)
        Log.d("login",number.get().toString() + "   "+ password.get())
        var user = User(number.get().toString().replace("[^0-9]".toRegex(), ""),password.get().toString())
        var advuser: AdvUser? =null
        try {
            advuser = applicationModule.login(user).execute().body()!!

        }
        catch (e: Exception){
            val snackbar1 =
                Snackbar.make(v, "USER DOESN'T EXIST", Snackbar.LENGTH_LONG)
            snackbar1.show()
        }
        if(advuser !=null && advuser.status=="ACTIVE"){
            Log.d("USER: User found",advuser!!.toString())
            SharedPrefHelper(v.context).setUser(user)
            ApplicationDatabase.getAppDataBase(context!!)?.advUserDao()?.insertUser(advuser)
            var fragmentDirection = RegistrationFragmentDirections.actionRegistrationFragmentToMainFragment(advuser)
            v.findNavController().navigate(fragmentDirection)
        }
        else
        {
            val snackbar1 =
                Snackbar.make(v, "USER DOESN'T EXIST", Snackbar.LENGTH_LONG)
            snackbar1.show()
        }

        //_navigateScreen.value = Event(R.id.action_registrationFragment_to_loginFragment)
    }

    override fun registration(v :View) {
        MainApplication.component.inject(this)
        var regex = Regex("[^0-9]")
        user.username = regex.replace(number.get().toString(),"")
        user.password = password.get()
        var registrationaction = applicationModule.sign_up(user).execute()
        if(!registrationaction.isSuccessful) {
            Snackbar.make(v,"Try again later",Snackbar.LENGTH_LONG)
        }
        else {
            var db =
                ApplicationDatabase.getAppDataBase(context)
            Log.d("registration", registrationaction.body().toString())
            db?.advUserDao()?.insertUser(registrationaction.body()!!)
            SharedPrefHelper(v.context).setUser(User(user.username,user.password))
            var fragmentDirection =
                RegistrationFragmentDirections.actionRegistrationFragmentToPinFragment()
            v.findNavController().navigate(fragmentDirection)
        }
    }
}


