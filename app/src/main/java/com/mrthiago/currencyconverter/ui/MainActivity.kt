package com.mrthiago.currencyconverter.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.mrthiago.currencyconverter.R
import com.mrthiago.currencyconverter.adapters.CurrencyChangedCallback
import com.mrthiago.currencyconverter.adapters.CurrencyRatesAdapter
import com.mrthiago.currencyconverter.databinding.ActivityMainBinding
import com.mrthiago.currencyconverter.utilities.MarginItemDecoration
import com.mrthiago.currencyconverter.utilities.extensions.hideKeyboard
import com.mrthiago.currencyconverter.utilities.states.CurrencyState
import com.mrthiago.currencyconverter.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CurrencyChangedCallback {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var currencyRatesAdapter: CurrencyRatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adapter List
        currencyRatesAdapter = CurrencyRatesAdapter(this)

        // Set the Adapter and LayoutManager
        binding.recyclerViewRates.apply {
            adapter = currencyRatesAdapter
            layoutManager = LinearLayoutManager(context)

            // Remove Row blinking and add padding between the rows
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            addItemDecoration(MarginItemDecoration(16))

            // On Scroll hide the keyboard
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        hideKeyboard()
                    }
                }
            })
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.currencyRates.collect { status ->
                when (status) {
                    is CurrencyState.Success -> {
                        // Hide noData view if visible
                        if (!binding.recyclerViewRates.isVisible) {
                            binding.recyclerViewRates.apply {
                                visibility = View.VISIBLE
                                binding.noDataTv.visibility = GONE
                            }
                        }
                        // Update the Adapter with the Rates
                        currencyRatesAdapter.data = status.resultData
                    }
                    is CurrencyState.NoData -> {
                        binding.noDataTv.apply {
                            visibility = View.VISIBLE
                            text = status.noDataMessage
                            binding.recyclerViewRates.visibility = GONE
                        }
                    }
                    is CurrencyState.Failure -> {
                        if (!TextUtils.isEmpty(status.errorMessage)) {
                            Snackbar.make(binding.root, status.errorMessage, Snackbar.LENGTH_SHORT).show()
                            if (status.errorMessage.equals(R.string.network_error)) {
                                mainViewModel.stopUpdates()
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Call the API to fetch the Rates
        mainViewModel.getRates()
        hideKeyboard()
    }

    override fun onPause() {
        super.onPause()
        // Stop Calling the API
        mainViewModel.stopUpdates()
    }

    // Callback from the Adapter (on item clicked and on amount changed)
    override fun onCurrencyChanged(code: String, amount: Int) {
        // Get new Rates
        mainViewModel.getRates(code, amount)

        // Scroll to the top of the list
        lifecycleScope.launch(Dispatchers.Main) {
            delay(100)
            if (binding.recyclerViewRates.size > 1) {
                (binding.recyclerViewRates.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 1)
            }
        }
    }
}