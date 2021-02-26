package com.mrthiago.currencyconverter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.mrthiago.currencyconverter.R
import com.mrthiago.currencyconverter.data.models.CurrencyData
import com.mrthiago.currencyconverter.utilities.extensions.placeCursorToEnd
import kotlinx.android.synthetic.main.item_rate.view.*
import java.text.DecimalFormat

class CurrencyRatesAdapter(private val onCurrencyChangedCallback: CurrencyChangedCallback) : RecyclerView.Adapter<CurrencyRatesAdapter.MyViewHolder>() {

    companion object {
        private val CURRENCY_FORMAT: DecimalFormat = DecimalFormat("#.##")
    }

    var data = listOf<CurrencyData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]

        holder.itemView.apply {
            // Labels and Image Icon
            code.text = item.code
            name.text = item.name
            flag.setImageResource(item.image)

            // EditText with Decimal formatter
            if (CURRENCY_FORMAT.format(item.amount).equals("0")) {
                // Remove 0 input
                rate.setText("")
            } else {
                rate.setText(CURRENCY_FORMAT.format(item.amount))
                rate.placeCursorToEnd()
            }

            // On Row clicked, move to top
            setOnClickListener {
                if (position != 0) {
                    onCurrencyChangedCallback.onCurrencyChanged(item.code, 1)
                }
            }

            // On EditText selection, if not at the top position, push it to top
            rate.onFocusChangeListener = OnFocusChangeListener { _, _ ->
                if (position != 0) {
                    onCurrencyChangedCallback.onCurrencyChanged(item.code, 1)
                }
            }

            // Get the Amount from the EditText
            if (position == 0) {
                rate.doAfterTextChanged { s ->
                    if (rate.hasFocus()) {
                        onCurrencyChangedCallback.onCurrencyChanged(item.code, parseStringToInt(s.toString()))
                    }
                }
            }
        }
    }

    private fun parseStringToInt(string: String): Int = string.toIntOrNull() ?: 0
}

