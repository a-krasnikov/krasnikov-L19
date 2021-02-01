package krasnikov.project.animationapp.ui.countries

import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.toAndroidPair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import krasnikov.project.animationapp.R
import krasnikov.project.animationapp.data.model.Country

class CountryAdapter(private val requestBuilder: RequestBuilder<PictureDrawable>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(),
    ListPreloader.PreloadModelProvider<Country> {

    var items: List<Country> = emptyList()
    var listener: CountryItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_country, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = items[position]

        holder.bind(country, requestBuilder)
        holder.itemView.setOnClickListener {
            val resources = holder.itemView.context.resources

            val pairImage = Pair<View, String>(
                holder.itemView.findViewById(R.id.ivFlag),
                resources.getString(R.string.transition_icon, country.name)
            ).toAndroidPair()

            val pairTitle = Pair<View, String>(
                holder.itemView.findViewById(R.id.tvName),
                resources.getString(R.string.transition_text, country.name)
            ).toAndroidPair()

            listener?.onCountryItemClick(country, pairImage, pairTitle)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getPreloadItems(position: Int): MutableList<Country> {
        return if (itemCount - position > 5)
            items.subList(position, position + 5).toMutableList()
        else
            mutableListOf(items[position])
    }

    override fun getPreloadRequestBuilder(item: Country): RequestBuilder<*>? {
        return requestBuilder.load(item.flag)
    }

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(country: Country, requestBuilder: RequestBuilder<PictureDrawable>) {
            with(itemView) {
                with(findViewById<TextView>(R.id.tvName)) {
                    text = country.name
                    transitionName = resources.getString(R.string.transition_text, country.name)
                }
                with(findViewById<ImageView>(R.id.ivFlag)) {
                    transitionName = resources.getString(R.string.transition_icon, country.name)
                    requestBuilder.load(country.flag).into(this)
                }
            }
        }
    }

    interface CountryItemClickListener {
        fun onCountryItemClick(
            country: Country,
            pairImage: android.util.Pair<View, String>,
            pairTitle: android.util.Pair<View, String>
        )
    }
}