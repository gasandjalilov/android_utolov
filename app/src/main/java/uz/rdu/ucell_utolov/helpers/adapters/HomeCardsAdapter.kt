package uz.rdu.ucell_utolov.helpers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderAdapter
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.fragments.HomeFragmentDirections
import uz.rdu.ucell_utolov.fragments.PinFragmentDirections
import uz.rdu.ucell_utolov.helpers.ApplicationDatabase
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import kotlin.random.Random

class HomeCardsAdapter(private val profiles: List<ProfileResponse>,context:Context?): CardSliderAdapter<HomeCardsAdapter.HomeViewHolder>()  {




    var list = mutableListOf(R.drawable.card_gradient_1,R.drawable.card_gradient_2,R.drawable.card_gradient_3,R.drawable.card_gradient_4,R.drawable.card_gradient_5,R.drawable.card_gradient_6)
    var db = ApplicationDatabase.getAppDataBase(context)



    override fun bindVH(holder: HomeViewHolder, position: Int) {
        val profile = profiles[position]
        var layout = holder.itemView.findViewById<ConstraintLayout>(R.id.card_layout) as ConstraintLayout
        var elementNumber = holder.itemView.findViewById<TextView>(R.id.card_element_number) as TextView
        var card_exp = holder.itemView.findViewById<TextView>(R.id.card_element_exp) as TextView
        var amount = holder.itemView.findViewById<TextView>(R.id.card_element_amount) as TextView
        var card = holder.itemView.findViewById<CardView>(R.id.card_element_cardview) as CardView

        var cardnum:String?
        if(profile.card_number.length>2) {
            cardnum = profile.card_number.replaceRange(4, 12, " **** **** ")
        }
        else {
            cardnum = profile.card_number
        }

        if(profile.card_number==""){
            var button = holder.itemView.findViewById<ImageButton>(R.id.button_add) as ImageButton
            holder.itemView.findViewById<TextView>(R.id.card_element_amount3).visibility = View.GONE
            holder.itemView.findViewById<TextView>(R.id.card_element_amount2).visibility = View.GONE
            button.visibility=View.VISIBLE
            card.setBackgroundResource(R.drawable.card_gradient_1)
            elementNumber.visibility=View.GONE
            card_exp.visibility=View.GONE
            amount.visibility=View.GONE
            button.setOnClickListener{
                val action = HomeFragmentDirections.actionHomeFragmentToCardActionFragment()
                it.findNavController().navigate(action)
            }
        }
        else {
            var card_amount = profile.balance?.toDouble()
            elementNumber.text = cardnum
            card_exp.text = profile.card_expire!!.addCharAtIndex('/',2)
            amount.text = String.format("%,d", card_amount?.toLong())

            var rand = Random
            var dbCard = db?.profileResponseDao()?.findByCardNumber(profile.card_number)
            if(dbCard?.card_color != null) {
                card.setBackgroundResource(list[rand.nextInt(list.size)])
                layout.setBackgroundResource(R.drawable.ic_card_background_1)
                //card.setBackgroundResource(dbCard.card_color!!)
            }
            else {
                card.setBackgroundResource(list[rand.nextInt(list.size)])
                layout.setBackgroundResource(R.drawable.ic_card_background_1)
            }
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_element, parent, false)

        return HomeViewHolder(view)
    }


    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view)


    fun String.addCharAtIndex(char: Char, index: Int) =
        StringBuilder(this).apply { insert(index, char) }.toString()


}