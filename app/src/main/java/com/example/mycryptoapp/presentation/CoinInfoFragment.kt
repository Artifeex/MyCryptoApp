package com.example.mycryptoapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mycryptoapp.R
import com.example.mycryptoapp.data.mappers.CoinInfoMapper
import com.example.mycryptoapp.databinding.FragmentCoinDetailBinding
import com.example.mycryptoapp.databinding.ItemCoinInfoBinding

class CoinInfoFragment : Fragment() {

    private var _binding: FragmentCoinDetailBinding? = null
    private val binding: FragmentCoinDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentCoinDetailBinding is null")

    private val viewModel by lazy {
        ViewModelProvider(this)[ListCoinsViewModel::class.java]
    }

    private var fromSymbol = UNDEFINED_FROM_SYMBOL


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun parseParams() {
        fromSymbol = requireArguments().getString(FROM_SYMBOL, UNDEFINED_FROM_SYMBOL)
    }

    private fun observeViewModel() {
        viewModel.getDetailCoinInfo(fromSymbol).observe(viewLifecycleOwner) {
            Glide
                .with(binding.root)
                .load(it.imageUrl)
                .into(binding.ivLogoCoin)

            binding.tvPrice.text = it.price
            binding.tvFromSymbol.text = it.fromSymbol
            binding.tvToSymbol.text = it.toSymbol
            binding.tvMinPrice.text = it.minPriceDay
            binding.tvMaxPrice.text = it.maxPriceDay
            binding.tvLastMarket.text = it.lastMarket
            binding.tvLastUpdate.text = it.lastUpdate
        }
    }


    companion object {

        private const val FROM_SYMBOL = "from_symbol"
        private const val UNDEFINED_FROM_SYMBOL = "undefined"

        fun newInstance(fromSymbol: String): Fragment {
            return CoinInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(FROM_SYMBOL, fromSymbol)
                }
            }
        }
    }
}