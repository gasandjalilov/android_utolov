package uz.rdu.ucell_utolov.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentArticleBinding
import uz.rdu.ucell_utolov.databinding.FragmentPayByMerchantBinding
import uz.rdu.ucell_utolov.helpers.adapters.ArticleAdapter
import uz.rdu.ucell_utolov.interfaces.api.ApiArticleInterface
import uz.rdu.ucell_utolov.modelviews.ArticleViewModel
import uz.rdu.ucell_utolov.modelviews.MainViewModel
import javax.inject.Inject

class ArticleFragment : Fragment() {

    private val model: MainViewModel by activityViewModels()
    lateinit var fragmentArticleBinding: FragmentArticleBinding
    var articleViewModel: ArticleViewModel = ArticleViewModel()

    @Inject
    lateinit var applicationModule: ApiArticleInterface



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        MainApplication.component.inject(this)
        fragmentArticleBinding =
            DataBindingUtil.inflate<FragmentArticleBinding>(
                inflater,
                R.layout.fragment_article,
                container,
                false
            )
        fragmentArticleBinding.vmodel = articleViewModel
        var recyclerView = fragmentArticleBinding.articleRecyclerview
        applicationModule.allNews().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                recyclerView.adapter = ArticleAdapter(it.toMutableList(),requireContext())
            },{
                Log.e("ArticleViewModel",it.message)
            })
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        return fragmentArticleBinding.root
    }


}