package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.islamkhsh.CardSliderViewPager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentCardActionBinding
import uz.rdu.ucell_utolov.helpers.adapters.CardsActionAdapter
import uz.rdu.ucell_utolov.helpers.adapters.HomeCardsAdapter
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.modelviews.CardActionViewModel
import uz.rdu.ucell_utolov.modelviews.HomeViewModel
import uz.rdu.ucell_utolov.modelviews.MainViewModel

class CardActionFragment : Fragment() {

    private val model: MainViewModel by activityViewModels()

    lateinit var cardActionViewModel: CardActionViewModel
    lateinit var cards: MutableList<ProfileResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        cardActionViewModel = CardActionViewModel()
        var cardsBinding: FragmentCardActionBinding =
            DataBindingUtil.inflate<FragmentCardActionBinding>(
                inflater,
                R.layout.fragment_card_action,
                container,
                false
            )
        cardsBinding.vmodel = cardActionViewModel
        model.cardListLive.observe(viewLifecycleOwner, Observer {
            cards = it.toMutableList()
            cards.add(ProfileResponse())
            val cardSliderViewPager =
                cardsBinding.root.findViewById(R.id.cardviewPager) as CardSliderViewPager
            cardSliderViewPager.adapter = CardsActionAdapter(cards, requireContext())
        })

        return cardsBinding.root
    }


}