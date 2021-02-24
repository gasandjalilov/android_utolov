package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentTransferBinding
import uz.rdu.ucell_utolov.databinding.FragmentUcellSubscriberBinding
import uz.rdu.ucell_utolov.modelviews.MainViewModel
import uz.rdu.ucell_utolov.modelviews.TransferViewModel
import uz.rdu.ucell_utolov.modelviews.UcellSubscriberViewModel


class UcellSubscriberFragment : Fragment() {


    lateinit var ucellViewModel: UcellSubscriberViewModel
    lateinit var fragmentUcellBinding: FragmentUcellSubscriberBinding
    private val model: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        ucellViewModel = UcellSubscriberViewModel()
        fragmentUcellBinding = DataBindingUtil.inflate<FragmentUcellSubscriberBinding>(
            inflater,
            R.layout.fragment_ucell_subscriber,
            container,
            false
        )
        fragmentUcellBinding.vmodel = ucellViewModel
        fragmentUcellBinding.mainModel = model
        return fragmentUcellBinding.root
    }


}