package uz.rdu.ucell_utolov.helpers.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.fragments.MerchantFragmentDirections
import uz.rdu.ucell_utolov.fragments.RegistrationFragmentDirections
import uz.rdu.ucell_utolov.models.merchantmodels.MerchantData


class MerchantAdapter(list: List<MerchantData>) :
    RecyclerView.Adapter<MerchantAdapter.ViewHolder>() {

    var merchants: List<MerchantData> = list


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        var text: TextView

        init {
            imageView = view.findViewById(R.id.merchant_img)
            text = view.findViewById(R.id.merchant_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.merchant_card, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.textView.text = merchants[position].name
        var url: Uri = Uri.parse(merchants[position].url)
        Picasso.get().load(url).transform(RoundedCornersTransformation(100, 20)).resize(250, 250)
            .centerCrop().into(holder.imageView)
        holder.text.text = merchants[position].merchant.name
        holder.imageView.setOnClickListener{
            var fragmentDirection =
                MerchantFragmentDirections.actionMerchantFragmentToPayByMerchantFragment(null,merchants[position])
            holder.itemView.findNavController().navigate(fragmentDirection)
        }
    }

    override fun getItemCount(): Int {
        return merchants.size
    }
}