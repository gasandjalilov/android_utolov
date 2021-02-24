package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.view.*
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentMobileCarrierPaymentBinding
import uz.rdu.ucell_utolov.helpers.adapters.CardsActionAdapter
import uz.rdu.ucell_utolov.helpers.adapters.PaymentCardsAdapter
import uz.rdu.ucell_utolov.modelviews.MainViewModel
import uz.rdu.ucell_utolov.modelviews.MobileCarrierPaymentViewModel


class MobileCarrierPaymentFragment : Fragment() {

    lateinit var mobileCarrierPaymentViewModel: MobileCarrierPaymentViewModel

    private val model: MainViewModel by activityViewModels()
    private val args: MobileCarrierPaymentFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this

        mobileCarrierPaymentViewModel = MobileCarrierPaymentViewModel()
        var mobileCarrierPaymentBinding: FragmentMobileCarrierPaymentBinding =
            DataBindingUtil.inflate<FragmentMobileCarrierPaymentBinding>(
                inflater,
                R.layout.fragment_mobile_carrier_payment,
                container,
                false
            )
        mobileCarrierPaymentBinding.vmodel = mobileCarrierPaymentViewModel
        mobileCarrierPaymentViewModel.number.set(args.number)
        mobileCarrierPaymentBinding.mobileCarrierNumber.addTextChangedListener(
            PhoneNumberFormattingTextWatcher()
        )
        val cardSliderViewPager = mobileCarrierPaymentBinding.viewPager
        cardSliderViewPager.adapter = PaymentCardsAdapter(model.cardListLive.value!!, requireContext())
        mobileCarrierPaymentViewModel.cardSliderViewPager=cardSliderViewPager

        return mobileCarrierPaymentBinding.root

    }

}