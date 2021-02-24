package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentMainBinding
import uz.rdu.ucell_utolov.databinding.FragmentPayByMerchantBinding
import uz.rdu.ucell_utolov.helpers.PatternInputFilter
import uz.rdu.ucell_utolov.helpers.adapters.HomeCardsAdapter
import uz.rdu.ucell_utolov.helpers.adapters.PaymentCardsAdapter
import uz.rdu.ucell_utolov.helpers.adapters.RoundedCornersTransformation
import uz.rdu.ucell_utolov.models.transaction.innertransaction.DBTransaction
import uz.rdu.ucell_utolov.modelviews.MainViewModel
import uz.rdu.ucell_utolov.modelviews.PayByMerchantViewModel
import java.util.regex.Pattern


class PayByMerchantFragment() : Fragment() {

    val payByMerchantViewModel: PayByMerchantViewModel by activityViewModels()
    private val args: PayByMerchantFragmentArgs by navArgs()
    lateinit var homeCardsAdapter: PaymentCardsAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var payByMerchantBinding: FragmentPayByMerchantBinding =
            DataBindingUtil.inflate<FragmentPayByMerchantBinding>(
                inflater,
                R.layout.fragment_pay_by_merchant,
                container,
                false
            )
        payByMerchantBinding.vmodel = payByMerchantViewModel
        payByMerchantViewModel.viewContext = requireContext()

        if (args.dbtransaction != null) {
            payByMerchantViewModel.url = args.dbtransaction?.url.toString()
            payByMerchantViewModel.account.set(args.dbtransaction?.account)
            payByMerchantViewModel.amount.set(args.dbtransaction?.amount.toString())
            payByMerchantViewModel.transactionPerformRequest.merchant_id =
                args.dbtransaction?.merchant_id
            Picasso.get().load(args.dbtransaction!!.url)
                .transform(RoundedCornersTransformation(100, 20))
                .resize(250, 250)
                .centerCrop().into(payByMerchantBinding.merchantImg)
        } else {
            payByMerchantViewModel.transactionPerformRequest.merchant_id =
                args.merchant?.merchant?.merchant_id
            payByMerchantViewModel.url = args.merchant!!.url
            Picasso.get().load(args.merchant!!.url).transform(RoundedCornersTransformation(100, 20))
                .resize(250, 250)
                .centerCrop().into(payByMerchantBinding.merchantImg)
            if (args?.merchant?.merchant?.account_prefix != null) {
                payByMerchantBinding.payByMerchantAccount.filters = arrayOf(
                    PatternInputFilter(
                        Pattern.compile(args?.merchant?.merchant?.account_regexp)
                    )
                )
                //payByMerchantBinding.payByMerchantAccount.filters = arrayOf(FilterMatcher(args?.merchant?.merchant?.account_regexp!!))
            }

        }
        model.cardListLive.observe(viewLifecycleOwner, Observer {
            homeCardsAdapter = PaymentCardsAdapter(model.cardListLive.value!!, requireContext())
            payByMerchantBinding.viewPager.adapter = homeCardsAdapter
            payByMerchantBinding.progressBar3.visibility = View.GONE
            payByMerchantViewModel.cardSliderViewPager = payByMerchantBinding.viewPager
        })



        return payByMerchantBinding.root
    }


}