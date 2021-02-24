package uz.rdu.ucell_utolov.helpers.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.fragments.HomeFragmentDirections
import uz.rdu.ucell_utolov.fragments.MerchantFragmentDirections
import uz.rdu.ucell_utolov.fragments.RegistrationFragmentDirections
import uz.rdu.ucell_utolov.models.merchantmodels.MerchantData
import uz.rdu.ucell_utolov.models.transaction.innertransaction.DBTransaction


class DBTransactionAdapterHomeScreeen(var context:Context,list: List<DBTransaction>) :
    RecyclerView.Adapter<DBTransactionAdapterHomeScreeen.ViewHolder>() {

    var merchants: List<DBTransaction> = list


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        var account: TextView
        var amount: TextView
        var card:CardView
        init {
            imageView = view.findViewById(R.id.transactionhistory_merchant_img)
            account = view.findViewById(R.id.transactionhistory_merchant_account)
            amount = view.findViewById(R.id.transactionhistory_merchant_ammount)
            card = view.findViewById(R.id.transactionhistory_merchant_card)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_merchant_card, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.textView.text = merchants[position].name
        var url: Uri = Uri.parse(merchants[position].url)
        Picasso.get().load(url).transform(RoundedCornersTransformation(100, 20)).resize(250, 250)
            .centerCrop().into(holder.imageView)
        holder.account.text = merchants[position].account
        holder.amount.text = context.resources.getText(R.string.home_history_amount).toString() + ": " +merchants[position].amount.toString()
        holder.card.setOnClickListener{
            var fragmentDirection = HomeFragmentDirections.actionHomeFragmentToPayByMerchantFragment(merchants[position],null)
            holder.itemView.findNavController().navigate(fragmentDirection)
        }
    }

    override fun getItemCount(): Int {
        return merchants.size
    }
}