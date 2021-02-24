package uz.rdu.ucell_utolov.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentMerchantBinding
import uz.rdu.ucell_utolov.helpers.adapters.MerchantAdapter
import uz.rdu.ucell_utolov.interfaces.api.ApiMerchantInterface
import uz.rdu.ucell_utolov.models.merchantmodels.MerchantCategory
import uz.rdu.ucell_utolov.models.merchantmodels.MerchantData
import uz.rdu.ucell_utolov.modelviews.MainViewModel
import uz.rdu.ucell_utolov.modelviews.MerchantViewModel
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import javax.inject.Inject
import kotlin.collections.HashMap


class MerchantFragment : Fragment() {

    lateinit var merchantViewModel: MerchantViewModel

    private val model: MainViewModel by activityViewModels()

    @Inject
    lateinit var merchantModule: ApiMerchantInterface

    lateinit var listMerchant: List<MerchantData>

    lateinit var merchantBinding: FragmentMerchantBinding


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MainApplication.component.inject(this)
        merchantViewModel = MerchantViewModel()

        merchantBinding =
            DataBindingUtil.inflate<FragmentMerchantBinding>(
                inflater,
                R.layout.fragment_merchant,
                container,
                false
            )
        merchantBinding.vmodel = merchantViewModel

        callMerchants()
        return merchantBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun callMerchants() {
        merchantModule.allMerchantsWithImg()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                var allObjects = response
                var setOfObjects: HashMap<String, MerchantData> = HashMap<String, MerchantData>()
                var uuid_mob = UUID.fromString("CB83C383-FA6B-447B-B5C8-7CAC534BCAA9")
                var uuid_int = UUID.fromString("5E5427FC-5E53-425E-8623-0774FEE3D88D")
                var uuid_ose = UUID.fromString("F0C2EFE0-8EBC-426E-A745-12F7980A9999")


                val byMobile: Predicate<MerchantData> = Predicate<MerchantData> { category -> category.merchantCategory.id == uuid_mob }
                val byInternet: Predicate<MerchantData> = Predicate<MerchantData> { category -> category.merchantCategory.id == uuid_int }
                val byIservice: Predicate<MerchantData> = Predicate<MerchantData> { category -> category.merchantCategory.id == uuid_ose }


                setMerchants(response.stream().filter(byInternet).collect(Collectors.toList()),merchantBinding.merchantListInternet)
                setMerchants(response.stream().filter(byIservice).collect(Collectors.toList()),merchantBinding.merchantListInternetService)
                setMerchants(response.stream().filter(byMobile).collect(Collectors.toList()),merchantBinding.merchantListOperator)
            },
                { error -> getMerchantMethodError(error) })
    }


    fun setMerchants(merchants: List<MerchantData>, recyclerView: RecyclerView) {
        this.listMerchant = merchants
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.HORIZONTAL
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_main_merchants
            )!!
        )
        //merchantRecyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = MerchantAdapter(listMerchant)
    }

    fun getMerchantMethodError(error: Throwable) {
        Log.d("Merchant Error", error.message)
    }

    fun setPopularMerchants(){

    }


}