package uz.rdu.ucell_utolov.modelviews

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import retrofit2.Response
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.dagger_implementations.module.ApiAuthModule
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.helpers.Event
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.interfaces.api.ApiAuthInterface
import uz.rdu.ucell_utolov.interfaces.LoginViewInterface
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.User
import java.lang.Exception
import javax.inject.Inject


class PhonePinViewModel():ViewModel(),LoginViewInterface {

    var user: User = User()
    var isAnimation = ObservableBoolean(true)
    private val _navigateScreen = MutableLiveData<Event<Any>>()
    val navigateScreen: LiveData<Event<Any>> = _navigateScreen
    val progressVisible = ObservableField(false)


    @Inject
    lateinit var applicationModule: ApiAuthInterface

    @Inject
    lateinit var context:Context

    override fun login(view: View) {
        MainApplication.component.inject(this)
        progressVisible.set(true)
        if(user.username.isNullOrEmpty() && user.password.isNullOrEmpty()){
            this.user =  SharedPrefHelper(view.context).getUser()
        }
        var advuser: AdvUser? =null
        try {
            advuser = applicationModule.login(user).execute().body()!!

        }
        catch (e: Exception){
            val snackbar1 =
                Snackbar.make(view, "USER DOESN'T EXIST", Snackbar.LENGTH_LONG)
            snackbar1.show()
        }
        if(advuser !=null && advuser.status=="ACTIVE"){
            progressVisible.set(false)
            Log.d("USER: User found",advuser!!.toString())
            progressVisible.set(false)
            SharedPrefHelper(view.context).setUser(user)
            ApplicationDatabase.getAppDataBase(context!!)?.advUserDao()?.insertUser(advuser)
        }
        else
        {
            progressVisible.set(false)
            val snackbar1 =
                Snackbar.make(view, "USER DOESN'T EXIST", Snackbar.LENGTH_LONG)
            snackbar1.show()
        }

        //_navigateScreen.value = Event(R.id.action_loginFragment_to_mainFragment)
    }



}