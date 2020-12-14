package uz.rdu.ucell_utolov.modelviews

import android.content.Context
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.helpers.Event
import uz.rdu.ucell_utolov.interfaces.UserPhoneRegistrationInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiAuthInterface
import uz.rdu.ucell_utolov.models.navigationmodels.UserPhoneRegistration
import javax.inject.Inject

class PinViewModel(userPhoneRegistration: UserPhoneRegistration): BaseObservable(),UserPhoneRegistrationInterface {

    var isAnimation = ObservableBoolean(true)
    private val _navigateScreen = MutableLiveData<Event<Any>>()
    val navigateScreen: LiveData<Event<Any>> = _navigateScreen

    @Inject
    lateinit var applicationModule: ApiAuthInterface

    @Inject
    lateinit var context: Context


    fun init(){
        MainApplication.component.inject(this)
    }



    override fun checkOTP(view: View) {
    }
}