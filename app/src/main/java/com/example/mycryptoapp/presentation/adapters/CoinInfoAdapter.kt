package com.example.mycryptoapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycryptoapp.R
import com.example.mycryptoapp.data.network.ApiFactory
import com.example.mycryptoapp.databinding.ItemCoinInfoBinding
import com.example.mycryptoapp.domain.CoinInfo
import com.example.mycryptoapp.presentation.adapters.CoinInfoViewHolder

class CoinInfoAdapter(
    private val context: Context
): ListAdapter<CoinInfo, CoinInfoViewHolder>(CoinItemDiffCallback()) {

    var onCoinClick: ((CoinInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val binding = ItemCoinInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CoinInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val currentCoin = currentList[position]
        holder.binding.tvPrice.text = currentCoin.price.toString()
        val symbolsTemplateStr = context.resources.getString(R.string.symbols_template)
        holder.binding.tvSymbols.text = String.format(
            symbolsTemplateStr,
            currentCoin.fromSymbol,
            currentCoin.toSymbol
        )
        Glide
            .with(holder.binding.root)
            .load(currentCoin.imageUrl)
            .into(holder.binding.ivLogoCoin)
        val lastUpdateTimeTemplate = context.resources.getString(R.string.last_update_template)
        holder.binding.tvLastUpdate.text = String.format(lastUpdateTimeTemplate, currentCoin.lastUpdate)
        holder.binding.root.setOnClickListener {
            onCoinClick?.invoke(currentCoin)
        }

    }



}