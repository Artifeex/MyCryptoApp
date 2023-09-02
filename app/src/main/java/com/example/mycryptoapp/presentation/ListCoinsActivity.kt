package com.example.mycryptoapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.example.mycryptoapp.R
import com.example.mycryptoapp.databinding.ActivityMainBinding
import com.example.mycryptoapp.presentation.adapters.CoinInfoAdapter

class ListCoinsActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[ListCoinsViewModel::class.java]
    }

    private lateinit var adapter: CoinInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAdapter()
        setupRecyclerView()
        observeViewModel()

    }

    private fun setupAdapter() {
        adapter = CoinInfoAdapter(this)
        adapter.onCoinClick = {
            if(isOnePaneMode()) {
                //Запускаем еще один экран и переходи на него
                startActivity(CoinDetailActivity.newIntent(this, it.fromSymbol))
            } else {
                //добавляем фрагмент во второю половину экрана
                launchFragment(it.fromSymbol)
            }
        }
    }

    private fun launchFragment(fromSymbol: String) {
        supportFragmentManager.popBackStack() //так как хочу, чтобы при нажатии кнопки назад фрагмент полностью исчезал с экрана, а не показывались предыдущие фрагменты
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_coin_info, CoinInfoFragment.newInstance(fromSymbol))
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        binding.rvCoinsList.adapter = adapter
        binding.rvCoinsList.itemAnimator = null

    }

    private fun observeViewModel() {
        viewModel.coinInfoList.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun isOnePaneMode(): Boolean {
        return binding.fragmentContainerCoinInfo == null
    }
}