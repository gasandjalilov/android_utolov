package uz.rdu.ucell_utolov.interfaces

import android.view.View

interface HomeViewInterface {
    fun addPack(size:Int)
    fun getNews()
    fun getAccount(v:View)
    fun hideAmount(v: View)
    fun getHistory()
}