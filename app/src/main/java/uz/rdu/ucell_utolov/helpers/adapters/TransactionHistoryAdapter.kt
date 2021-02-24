package uz.rdu.ucell_utolov.helpers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderAdapter
import com.squareup.picasso.Picasso
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.models.merchantmodels.MerchantData
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.models.transactionhistorymodels.TransactionHistoryPaymentsResponse

class TransactionHistoryAdapter(private val transactionHistoryPaymentsResponse: List<TransactionHistoryPaymentsResponse>, context: Context): RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>()   {

    var history: List<TransactionHistoryPaymentsResponse> = transactionHistoryPaymentsResponse


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        var imageView: ImageView
        var account: TextView
        var ammount: TextView
        var date: TextView
        init {
            imageView = view.findViewById(R.id.transactionhistory_image)
            account = view.findViewById(R.id.transactionhistory_account)
            ammount = view.findViewById(R.id.transactionhistory_ammount)
            date = view.findViewById(R.id.transactionhistory_date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_history, parent, false)


        return TransactionHistoryAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var transaction = history[position]
        holder.account.text = transaction.merchant_name + "  -  "+ transaction.account
        holder.ammount.text = transaction.amount
        holder.date.text = transaction.pay_date
        Picasso.get().load(transaction.merchant_id).transform(RoundedCornersTransformation(100, 20)).resize(250, 250)
            .centerCrop().into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return history.size
    }
}