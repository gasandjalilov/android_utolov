package uz.rdu.ucell_utolov.modelviews

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.runBlocking
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.fragments.HomeFragmentDirections
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.interfaces.MainViewModelInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiProfileInterface
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.profilemodels.ProfileRequest
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import javax.inject.Inject


class MainViewModel(): MainViewModelInterface, ViewModel() {

    @Inject
    lateinit var applicationModule: ApiProfileInterface

    @Inject
    lateinit var context:Context

    lateinit var navController:NavController


    var frameVisibility = ObservableField(View.INVISIBLE)


    init {
        frameVisibility.set(View.GONE)

        MainApplication.component.inject(this)
    }

    fun getAsyncProfile() = runBlocking {
        getProfile()
    }

    override fun getProfile(): MutableList<ProfileResponse> {
        var request = ProfileRequest(account()!!.username!!)
        var resp = applicationModule.retrieve(request)
        Log.d("MainViewModel", "Request: " + request.toString() + " Response: " + resp.toString())
        var response = resp.execute()
        Log.d("Response", response.toString())

        if(response.code()==200) {
            var profileResponse = response.body()!!.body
            return profileResponse!!.toMutableList()
        }
        else{
            throw Exception("Profile corrupted")
        }
    }

    fun setThisNavController(navController: NavController){
        this.navController = navController
    }


    override fun account(): AdvUser? {
        var db =
            ApplicationDatabase.getAppDataBase(context!!)
        return db?.advUserDao()
            ?.findByUsername(SharedPrefHelper(context).getUser().username!!)
    }

    override fun myCardsButtonAction(v: View) {
        frameVisibility.set(View.GONE)
        val action = HomeFragmentDirections.actionHomeFragmentToCardActionFragment()
        navController.navigateUp()
        navController?.navigate(action)

    }

    override fun transactionButtonAction(v: View) {
        frameVisibility.set(View.GONE)
        val action = HomeFragmentDirections.actionHomeFragmentToPaymentFragment()
        navController.navigateUp()
        navController?.navigate(action)

    }

    override fun paymentButtonAction(v: View) {
        frameVisibility.set(View.GONE)
        val action = HomeFragmentDirections.actionHomeFragmentToPaymentFragment()
        navController.navigateUp()
        navController?.navigate(action)

    }


}