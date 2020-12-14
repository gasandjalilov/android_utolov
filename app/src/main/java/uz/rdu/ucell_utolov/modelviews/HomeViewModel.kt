package uz.rdu.ucell_utolov.modelviews

import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import uz.rdu.ucell_utolov.interfaces.HomeViewInterface
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.models.view.HomeFragmentModel

class HomeViewModel(user:AdvUser,profile:List<ProfileResponse>): ViewModel(),HomeViewInterface {

    var homeView:HomeFragmentModel
    var amount:Double? = 0.0

    init {
        profile.forEach{
            Log.d("Body",it.toString())
            //amount = amount+ it.balance.toDouble()
            var balance:String? = it.balance
            if(balance == null)
                balance=0.0.toString()

            amount = balance.toDouble()
        }
        homeView = HomeFragmentModel(String.format ("%.2f",amount),profile,user)
    }

    override fun hideAmount() {
        TODO("Not yet implemented")
    }

    override fun addPack(size: Int) {
        TODO("Not yet implemented")
    }

    override fun getNews() {
        TODO("Not yet implemented")
    }

    override fun getAccount(v:View) {
    }
}