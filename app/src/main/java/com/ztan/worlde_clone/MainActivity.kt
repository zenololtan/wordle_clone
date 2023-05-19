package com.ztan.worlde_clone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.children

class MainActivity : AppCompatActivity() {
    private var parentLinearLayout: LinearLayout? = null
    private var currentRow: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parentLinearLayout = findViewById(R.id.linear_layout)
        addField(null)
    }

    private fun disableRow(row: RelativeLayout) {
        for (child in row.children) {
            child.isEnabled = false
        }
    }

    fun addField(view: View?) {
        if (currentRow >= 5)
            return
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.word_prompt, null)
        parentLinearLayout!!.addView(rowView, currentRow)
        if (currentRow > 0)
            disableRow(parentLinearLayout!!.getChildAt(currentRow - 1) as RelativeLayout)
        currentRow++
    }
}