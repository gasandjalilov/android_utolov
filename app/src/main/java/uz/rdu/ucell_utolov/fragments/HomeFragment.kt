package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.islamkhsh.CardSliderViewPager
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentHomeBinding
import uz.rdu.ucell_utolov.helpers.adapters.HomeCardsAdapter
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.modelviews.HomeViewModel
import uz.rdu.ucell_utolov.modelviews.MainViewModel


class HomeFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel

    private val model: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var cards = model.getAsyncProfile()
        if(cards.size==0){
            cards.add(ProfileResponse())
        }
        homeViewModel = HomeViewModel(model.account()!!, cards)
        var homeBinding: FragmentHomeBinding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        val cardSliderViewPager = homeBinding.root.findViewById(R.id.viewPager) as CardSliderViewPager


        homeBinding.vmodel = homeViewModel

        cardSliderViewPager.adapter = HomeCardsAdapter(cards,requireContext())
        return homeBinding.root
    }

}