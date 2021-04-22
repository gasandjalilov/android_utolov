package uz.rdu.ucell_utolov.helpers.adapters

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.models.transactionhistorymodels.TransactionHistoryPaymentsResponse


class TransactionHistoryAdapter(
    private val transactionHistoryPaymentsResponse: List<TransactionHistoryPaymentsResponse>?,
    var context: Context,
    var mainScreen: Boolean
): RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>()   {

    var history: List<TransactionHistoryPaymentsResponse>? = transactionHistoryPaymentsResponse


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {

        var transactionhistory_card: CardView
        var imageView: ImageView
        var account: TextView
        var ammount: TextView
        var date: TextView
        init {
            transactionhistory_card = view.findViewById(R.id.transactionhistory_card)
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
        try{
            var transaction = history?.get(position)
            holder.account.text = transaction?.merchant_name + "  -  "+ transaction?.account
            holder.ammount.text = transaction?.amount
            holder.date.text = transaction?.pay_date
            Picasso.get().load(transaction?.merchant_id).transform(
                RoundedCornersTransformation(
                    100,
                    20
                )
            ).resize(250, 250)
                .centerCrop().into(holder.imageView)

            if(position == history!!.size.minus(1)){
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(getPixelValue(context,10), getPixelValue(context,10), getPixelValue(context,10), 250);
                params.marginEnd==250
                holder.transactionhistory_card.setLayoutParams(params)
                }
        }
        catch (e: Exception){
            Log.e("Error", e.message)
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        if(mainScreen){
        var size = history?.size
        if(history?.size!! >3) return 3
        else return history?.size!!}
        else{
            return history!!.size
        }
    }

    fun getPixelValue(context: Context, dimenId: Int): Int {
        val resources: Resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dimenId.toFloat(),
            resources.getDisplayMetrics()
        ).toInt()
    }
}