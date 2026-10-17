package com.example.vangtichai

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var amount = 0L
    private lateinit var tvAmount: TextView
    private val noteViews = LinkedHashMap<Int, TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAmount = findViewById(R.id.tvAmount)
        noteViews[500] = findViewById(R.id.note_500)
        noteViews[100] = findViewById(R.id.note_100)
        noteViews[50] = findViewById(R.id.note_50)
        noteViews[20] = findViewById(R.id.note_20)
        noteViews[10] = findViewById(R.id.note_10)
        noteViews[5] = findViewById(R.id.note_5)
        noteViews[2] = findViewById(R.id.note_2)
        noteViews[1] = findViewById(R.id.note_1)

        val digitKeys = mapOf(
            R.id.key0 to 0, R.id.key1 to 1, R.id.key2 to 2, R.id.key3 to 3,
            R.id.key4 to 4, R.id.key5 to 5, R.id.key6 to 6, R.id.key7 to 7,
            R.id.key8 to 8, R.id.key9 to 9
        )
        for ((id, digit) in digitKeys) {
            findViewById<TextView>(id).setOnClickListener { appendDigit(digit) }
        }
        findViewById<TextView>(R.id.keyClear).setOnClickListener {
            amount = 0
            render()
        }

        amount = savedInstanceState?.getLong(KEY_AMOUNT) ?: 0L
        render()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_AMOUNT, amount)
    }

    private fun appendDigit(digit: Int) {
        val next = amount * 10 + digit
        if (next <= MAX_AMOUNT) {
            amount = next
            render()
        }
    }

    private fun render() {
        tvAmount.text = if (amount == 0L) {
            getString(R.string.taka_label)
        } else {
            getString(R.string.taka_format, amount.toString())
        }
        var remaining = amount
        for ((note, view) in noteViews) {
            view.text = getString(R.string.note_format, note, remaining / note)
            remaining %= note
        }
    }

    companion object {
        private const val KEY_AMOUNT = "amount"
        private const val MAX_AMOUNT = 999_999_999L
    }
}
