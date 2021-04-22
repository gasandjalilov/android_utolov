package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentHistoryBinding
import uz.rdu.ucell_utolov.databinding.FragmentHomeBinding
import uz.rdu.ucell_utolov.helpers.adapters.TransactionHistoryAdapter
import uz.rdu.ucell_utolov.models.transactionhistorymodels.TransactionHistoryPaymentsResponse
import uz.rdu.ucell_utolov.modelviews.HistoryViewModel
import uz.rdu.ucell_utolov.modelviews.MainViewModel

class HistoryFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val HistoryViewModel:HistoryViewModel by activityViewModels()
    lateinit var historyBinding: FragmentHistoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        historyBinding = DataBindingUtil.inflate<FragmentHistoryBinding>(
            inflater,
            R.layout.fragment_history,
            container,
            false
        )
        historyBinding.historyvmodel = HistoryViewModel
        historyBinding.mainvmodel = mainViewModel

        mainViewModel.transactionHistoryPayment.observe(viewLifecycleOwner, Observer {
            setTransactionHistory(it.body)
        })



        return historyBinding.root
    }



    fun setTransactionHistory(transactions: List<TransactionHistoryPaymentsResponse>?) {
        historyBinding.transactions.adapter = TransactionHistoryAdapter(transactions, requireContext(),false)
    }
}