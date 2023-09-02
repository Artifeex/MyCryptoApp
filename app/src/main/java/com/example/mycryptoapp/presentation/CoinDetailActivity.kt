package com.example.mycryptoapp.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycryptoapp.R

class CoinDetailActivity : AppCompatActivity() {

    private var fromSymbol: String = UNDEFINED_FROM_SYMBOL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)
        parseIntent()
        //Только при первом запуске экрана мы создаем фрагмент самостоятельно и запускаем его. Так как после, например, переворота система сама создаем фрагмент
        //и запускает его
        if(savedInstanceState == null) {
            launchFragment()
        }
    }

    private fun parseIntent() {
        fromSymbol = intent.getStringExtra(FROM_SYMBOL) ?: throw RuntimeException("Did not provide FROM SYMBOL in intent CoinDetailActivity")
    }

    private fun launchFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_coin_info, CoinInfoFragment.newInstance(fromSymbol))
            .commit()
    }

    companion object {

        private const val FROM_SYMBOL = "from_symbol"
        private const val UNDEFINED_FROM_SYMBOL = "undefined"

        fun newIntent(context: Context, fromSymbol: String): Intent {
            return Intent(context, CoinDetailActivity::class.java).apply {
                putExtra(FROM_SYMBOL, fromSymbol)
            }
        }

    }
}