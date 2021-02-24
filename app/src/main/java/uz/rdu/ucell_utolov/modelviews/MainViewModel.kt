package uz.rdu.ucell_utolov.modelviews

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.fragments.HomeFragmentDirections
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.interfaces.MainViewModelInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiMerchantInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiProfileInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiUcellInteraface
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.merchantmodels.Merchant
import uz.rdu.ucell_utolov.models.profilemodels.ProfileRequest
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.models.transactionhistorymodels.TransactionHistoryRequest
import uz.rdu.ucell_utolov.models.ucell.Balances
import uz.rdu.ucell_utolov.models.ucell.Data
import uz.rdu.ucell_utolov.models.ucell.UcellResponse
import java.lang.Exception
import javax.inject.Inject


class MainViewModel : MainViewModelInterface, ViewModel() {

    @Inject
    lateinit var applicationModule: ApiProfileInterface

    @Inject
    lateinit var ucellModule:ApiUcellInteraface

    @Inject
    lateinit var context: Context

    lateinit var navController: NavController

    lateinit var cardList: MutableList<ProfileResponse>

    var balances: MutableLiveData<Balances> = MutableLiveData()
    var ucell_profile: MutableLiveData<UcellResponse> = MutableLiveData()
    var cardListLive: MutableLiveData<List<ProfileResponse>> = MutableLiveData()

    var frameVisibility = ObservableField(View.INVISIBLE)

    var user: AdvUser

    var balance = ObservableField<String>()

    var loading = ObservableBoolean()

    init {
        frameVisibility.set(View.GONE)
        MainApplication.component.inject(this)
        user = account()!!
        if(!this::cardList.isInitialized) {
            getProfile()
            setBalance()
        }
    }


    override fun getProfile() {
        Log.d("getProfileRequest","MainViewModel")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loading.set(true)
                var request = ProfileRequest(account()!!.username!!)
                var resp = applicationModule.retrieve(request).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    cardListLive.value = it.body!!
                    loading.set(false)
                },{
                    Log.e("getProfile",it.message)
                })
            }
            catch (e:Exception){
                throw Exception("PROFILE ERROR")
            }

        }
    }

    fun setThisNavController(navController: NavController) {
        this.navController = navController
    }


    override fun account(): AdvUser? {
        return SharedPrefHelper(context).getUserObject()
    }

    override fun myCardsButtonAction(v: View) {
        frameVisibility.set(View.GONE)
        val action = HomeFragmentDirections.actionHomeFragmentToCardActionFragment()
        navController.navigateUp()
        navController.navigate(action)

    }

    fun myProfile(v: View) {
        frameVisibility.set(View.GONE)
        val action = HomeFragmentDirections.actionHomeFragmentToUcellSubscriberFragment()
        navController.navigateUp()
        navController.navigate(action)

    }

    override fun transactionButtonAction(v: View) {
        frameVisibility.set(View.GONE)
        val action = HomeFragmentDirections.actionHomeFragmentToTransferFragment()
        navController.navigateUp()
        navController.navigate(action)

    }

    override fun paymentButtonAction(v: View) {
        frameVisibility.set(View.GONE)
        val action = HomeFragmentDirections.actionHomeFragmentToMerchantFragment()
        navController.navigateUp()
        navController.navigate(action)

    }

    override fun upointsAction(v: View) {
        frameVisibility.set(View.GONE)
        val action = HomeFragmentDirections.actionHomeFragmentToUballsFragment()
        navController.navigateUp()
        navController.navigate(action)
    }


    fun setBalance(){
        //var string = context.resources(R.string.home_balance)

        ucellModule.retrieveLastInP2P(TransactionHistoryRequest(user.username!!))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                balance.set(response.availBalance)
                castToBalance(response.data)
                ucell_profile.value = response
            },{error ->
                Log.e("Error Recieved",error.message)
            }
            )

    }

    fun castToBalance(data: List<Data>?){
        var balances = Balances()
        data?.forEach {
            when(it.balanceId){
                "682" -> balances.dataBalance1 = it.availableBalance
                "683" -> balances.dataBalance2 = it.availableBalance
                "696" -> balances.dataBalance3 = it.availableBalance
                "699" -> balances.dataBalance4 = it.availableBalance
                "706" -> balances.dataBalance5 = it.availableBalance
                "654" -> balances.voiceBalance1 = it.availableBalance
                "684" -> balances.voiceBalance2 = it.availableBalance
                "669" -> balances.voiceBalance3 = it.availableBalance
            }
        }
        this.balances.value = balances
    }

}