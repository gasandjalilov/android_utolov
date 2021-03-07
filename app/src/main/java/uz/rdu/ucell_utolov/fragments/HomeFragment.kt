package uz.rdu.ucell_utolov.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderViewPager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentHomeBinding
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.helpers.adapters.*
import uz.rdu.ucell_utolov.interfaces.api.ApiMerchantInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiProfileInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiTransactionHistoryInteraface
import uz.rdu.ucell_utolov.models.merchantmodels.Merchant
import uz.rdu.ucell_utolov.models.merchantmodels.MerchantData
import uz.rdu.ucell_utolov.models.profilemodels.ProfileRequest
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.models.transaction.innertransaction.DBTransaction
import uz.rdu.ucell_utolov.models.transactionhistorymodels.TransactionHistoryPayment
import uz.rdu.ucell_utolov.models.transactionhistorymodels.TransactionHistoryPaymentsResponse
import uz.rdu.ucell_utolov.models.transactionhistorymodels.TransactionHistoryRequest
import uz.rdu.ucell_utolov.modelviews.HomeViewModel
import uz.rdu.ucell_utolov.modelviews.MainViewModel
import java.lang.Exception
import javax.inject.Inject


class HomeFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel

    lateinit var model: MainViewModel

    @Inject
    lateinit var merchantModule: ApiMerchantInterface

    @Inject
    lateinit var applicationModule: ApiProfileInterface

    @Inject
    lateinit var transactionHistory: ApiTransactionHistoryInteraface

    lateinit var homeCardsAdapter: HomeCardsAdapter
    lateinit var listMerchant: List<MerchantData>

    lateinit var merchantRecyclerView: RecyclerView
    lateinit var historyRecyclerView: RecyclerView
    var history: List<TransactionHistoryPaymentsResponse>? = null
    lateinit var homeBinding: FragmentHomeBinding
    lateinit var db: ApplicationDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MainApplication.component.inject(this)
        model = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        if (model.ucell_profile.value == null) {
            model.initial()
        }
        homeBinding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        db = ApplicationDatabase.getAppDataBase(requireContext())!!
        /*
            GlobalScope.launch(Dispatchers.Main) {
                homeBinding.progressBar.visibility= View.VISIBLE
                CoroutineScope(coroutineContext).async {
                    var response = applicationModule.retrieve(
                        ProfileRequest(user!!.username!!)
                    )
                    response.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { response ->
                            if (response.body!!.isEmpty()) {
                                cards.add(ProfileResponse())

                            } else {
                                model.cardListLive.value = response.body.toMutableList()
                                setDataToAdapter(response.body.toMutableList())
                            }

                        }
                }
            }

         */

        model.cardListLive.observe(viewLifecycleOwner, Observer {
            setDataToAdapter(it.toMutableList())
        })
        homeViewModel = HomeViewModel(model.account()!!)
        homeViewModel.viewModel = model
        homeBinding.mvmodel = model
        homeBinding.vmodel = homeViewModel
        homeViewModel.homeBinding = homeBinding
        merchantRecyclerView = homeBinding.homeMerchants
        historyRecyclerView = homeBinding.transactions
        callMerchants()
        callHistory()
        return homeBinding.root
    }


    fun setDataToAdapter(data: MutableList<ProfileResponse>) {
        homeCardsAdapter = HomeCardsAdapter(data, this.context)
        val cardSliderViewPager =
            homeBinding.root.findViewById(R.id.viewPager) as CardSliderViewPager

        homeViewModel.profile = data
        homeViewModel.initialData()
        cardSliderViewPager.adapter = homeCardsAdapter
    }

    fun callMerchants() {
        var historyList = db.transactionDao().allSaved()
        if (historyList.isNotEmpty()) {
            homeBinding.homeMerchants.visibility = View.VISIBLE
            homeBinding.homeTextHistory.visibility = View.VISIBLE

            setHistoryTrans(historyList.toList())
        } else {
            homeBinding.homeTextHistory.visibility = View.GONE
            homeBinding.homeMerchants.visibility = View.GONE

        }
        /*
        CoroutineScope(coroutineContext).async  {
            merchantModule.allMerchantsWithImg()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response -> setMerchants(response) },
                    { error -> getMerchantMethodError(error) })
        }
         */
    }

    fun callHistory() {
        model.transactionHistoryPayment.observe(viewLifecycleOwner, Observer {
            setTransactionHistory(it.body)

        })
    }


    fun setHistoryTrans(merchants: List<DBTransaction>) {
        merchantRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val dividerItemDecoration = DividerItemDecoration(
            merchantRecyclerView.context,
            LinearLayoutManager.HORIZONTAL
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_main_merchants
            )!!
        )
        merchantRecyclerView.adapter = DBTransactionAdapterHomeScreeen(requireContext(), merchants)
    }

    fun setMerchants(merchants: List<MerchantData>) {
        this.listMerchant = merchants
        merchantRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val dividerItemDecoration = DividerItemDecoration(
            merchantRecyclerView.context,
            LinearLayoutManager.HORIZONTAL
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_main_merchants
            )!!
        )
        merchantRecyclerView.adapter = MerchantAdapterHomeScreeen(listMerchant)
    }

    fun getMerchantMethodError(error: Throwable) {
        Log.d("Merchant Error", error.message)
    }

    fun setTransactionHistory(transactions: List<TransactionHistoryPaymentsResponse>?) {
        this.history = transactions
        historyRecyclerView.adapter = TransactionHistoryAdapter(transactions, requireContext(),true)
    }


}