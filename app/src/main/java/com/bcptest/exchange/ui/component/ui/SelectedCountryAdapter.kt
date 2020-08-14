package com.bcptest.exchange.ui.component.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bcptest.exchange.R
import com.bcptest.exchange.ui.component.entity.Country

interface OnClickItem {
    fun onClickItem(country: Country)
}

class SelectedCountryAdapter(private val countries: List<Country>) :
    RecyclerView.Adapter<SelectedCountryAdapter.SelectedCountryAdapterViewHolder>() {

    private var mOnClickItem: OnClickItem? = null
    private var context: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedCountryAdapterViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return SelectedCountryAdapterViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = countries.size

    override fun onBindViewHolder(holder: SelectedCountryAdapterViewHolder, position: Int) {
        val country: Country = countries[position]
        holder.bind(country, context as Context)

        holder.itemView.setOnClickListener {
            mOnClickItem?.onClickItem(country)
        }

    }

    class SelectedCountryAdapterViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.view_exchange_country, parent, false)) {

        private var txtCountry: TextView = itemView.findViewById(R.id.txtCountry)
        private var txtExchange: TextView = itemView.findViewById(R.id.txtExchange)
        private var imgCountry: ImageView = itemView.findViewById(R.id.imgCountry)

        fun bind(country: Country, context: Context) {

            txtCountry.text = country.country
            txtExchange.text =
                "1 SOL = ".plus(country.valor.toString()).plus(" ").plus(country.money)
            val resourceId: Int =
                context.applicationContext.resources.getIdentifier(
                    country.image,
                    "drawable",
                    context.applicationContext.packageName
                )
            imgCountry.setImageResource(resourceId)

        }
    }

    fun setOnClickItem(onClickItem: OnClickItem) {
        mOnClickItem = onClickItem
    }

}