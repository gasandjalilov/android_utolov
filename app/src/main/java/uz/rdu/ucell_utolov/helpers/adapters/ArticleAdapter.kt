package uz.rdu.ucell_utolov.helpers.adapters

import android.content.Context
import android.graphics.drawable.Animatable
import android.net.Uri
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
import uz.rdu.ucell_utolov.models.articlemodels.Article

class ArticleAdapter(var articles: MutableList<Article>, context: Context) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        var title: TextView
        var text: TextView
        var card: CardView
        init {
            imageView = view.findViewById(R.id.article_image)
            title = view.findViewById(R.id.article_title)
            card = view.findViewById(R.id.article_card)
            text = view.findViewById(R.id.article_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_element, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var url: Uri = Uri.parse(articles[position].url)
        Picasso.get().load(url).centerCrop().fit().into(holder.imageView)
        holder.title.text = articles[position].title
        holder.text.text = articles[position].text
        var vis:Boolean = false

        if(position==articles.size-1){
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 250);
            holder.card.setLayoutParams(params)
        }

        holder.card.setOnClickListener {
            if (!vis) {
                holder.text.visibility = View.VISIBLE
                vis=true
            }
            else{
                holder.text.visibility = View.GONE
                vis=false
                holder.card.invalidate()

            }
        }
    }


}

