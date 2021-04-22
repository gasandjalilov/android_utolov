package uz.rdu.ucell_utolov.modelviews

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.databinding.FragmentHomeBinding
import uz.rdu.ucell_utolov.fragments.HomeFragmentDirections
import uz.rdu.ucell_utolov.fragments.RegistrationFragmentDirections
import uz.rdu.ucell_utolov.interfaces.HomeViewInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiTransactionHistoryInteraface
import uz.rdu.ucell_utolov.interfaces.api.ApiTransactionPerform
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.profilemodels.ProfileRequest
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.models.transactionhistorymodels.TransactionHistoryRequest
import uz.rdu.ucell_utolov.models.view.HomeFragmentModel
import java.lang.Exception
import javax.inject.Inject

class HomeViewModel(var user: AdvUser) : ViewModel(),
    HomeViewInterface {

    lateinit var homeView: MutableLiveData<HomeFragmentModel>
    lateinit var profile:List<ProfileResponse>
    lateinit var viewModel: MainViewModel
    var amount: Double? = 0.0
    var nonDigits = Regex("[^\\d]")
    var number: ObservableField<String> = ObservableField<String>()
    lateinit var homeBinding: FragmentHomeBinding
    var visible_amount = ObservableField<Int>(View.VISIBLE)
    var observable_amount:ObservableField<String> = ObservableField()

    var observableHistory:ObservableField<List<TransactionHistoryRequest>> = ObservableField()

    @Inject
    lateinit var applicationModule: ApiTransactionHistoryInteraface

    @Inject
    lateinit var context: Context



    init {
        MainApplication.component.inject(this)
    }

    fun onRefresh() {
        viewModel.getProfile()

    }

    fun initialData(){
        viewModelScope.launch {
            amount = 0.0
            profile.forEach {
                Log.d("Body", it.toString())
                //amount = amount+ it.balance.toDouble()
                var balance: String? = it.balance
                if (balance == null)
                    balance = 0.0.toString()

                amount = amount?.plus(balance.toDouble())
            }
            homeView = MutableLiveData(HomeFragmentModel(String.format("%.2f", amount), profile, user))
            observable_amount.set(homeView.value?.amount)

        }
    }


    fun mobileCarrierNumber(s: CharSequence) {
        if (s.toString().replace(nonDigits, "").length == 12) {
            number.set(s.toString().replace(nonDigits, ""))

        }
    }


    fun moveToPayment(v: View) {
        var fragmentDirection =
            HomeFragmentDirections.actionHomeFragmentToMobileCarrierPaymentFragment(number = number.get()!!)
        v.findNavController().navigate(fragmentDirection)
    }

    override fun hideAmount(v:View) {
        if (visible_amount.get() == View.VISIBLE)
        {
            visible_amount.set(View.INVISIBLE)
            homeView.value?.amount = ""
            observable_amount.set("")
        }

        else {
            visible_amount.set(View.VISIBLE)
            homeView.value?.amount = String.format("%.2f", amount)
            observable_amount.set(homeView.value?.amount)
        }
    }


    override fun getHistory(v:View) {
        var fragmentDirection =
            HomeFragmentDirections.actionHomeFragmentToHistoryFragment()
        v.findNavController().navigate(fragmentDirection)
    }

    override fun addPack(size: Int) {
        TODO("Not yet implemented")
    }

    override fun getNews(v: View) {
        var fragmentDirection =
            HomeFragmentDirections.actionHomeFragmentToArticleFragment()
        v.findNavController().navigate(fragmentDirection)
    }

    override fun getAccount(v: View) {
    }
}