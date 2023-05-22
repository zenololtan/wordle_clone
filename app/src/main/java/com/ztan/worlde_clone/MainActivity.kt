package com.ztan.worlde_clone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import com.ztan.worlde_clone.databinding.ActivityMainBinding
import com.ztan.worlde_clone.models.State

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: MainActivityViewModel by viewModels()
    private var parentLinearLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parentLinearLayout = binding.linearLayout
        bindButtons()
        updateGameStatus(vm.getState())
    }

    private fun bindButtons() = with(binding) {
        addButton.setOnClickListener { addField() }
        resetButton.setOnClickListener {
            vm.resetGame(binding.linearLayout)
            updateGameStatus(vm.getState())
        }
        edt.setOnKeyListener(View.OnKeyListener{_, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                addField()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun updateGameStatus(state: State) = when(state) {
        State.WON -> {
            binding.textStatus.text = "You Won!"
            binding.textStatus.visibility = View.VISIBLE
            binding.resetButton.visibility = View.VISIBLE
            binding.textStatus.bringToFront()
            binding.resetButton.bringToFront()
            binding.edt.isEnabled = false
            binding.addButton.isEnabled = false
        }
        State.LOST -> {
            binding.textStatus.text = "You Lost"
            binding.textStatus.visibility = View.VISIBLE
            binding.resetButton.visibility = View.VISIBLE
            binding.textStatus.bringToFront()
            binding.resetButton.bringToFront()
            binding.edt.isEnabled = false
            binding.addButton.isEnabled = false
        }
        State.PLAYING -> {
            binding.textStatus.visibility = View.GONE
            binding.resetButton.visibility = View.GONE
            binding.edt.isEnabled = true
            binding.addButton.isEnabled = true
        }
    }

    private fun validInput(str: String): Boolean {
        if (str.isEmpty() || str.length != 5)
            return false
        for (letter in str) {
            if (letter !in 'A'..'Z' && letter !in 'a'..'z')
                return false
        }
        return true
    }
    private fun addField() {
        val txt: String = binding.edt.text.toString().lowercase()
        if (!validInput(txt)) {
            Toast.makeText(this@MainActivity, "INVALID INPUT", Toast.LENGTH_SHORT).show()
            return
        }
        binding.edt.text.clear()
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: RelativeLayout = inflater.inflate(R.layout.word_line, null) as RelativeLayout
        vm.addLine(rowView, txt)
        parentLinearLayout!!.addView(rowView, parentLinearLayout!!.childCount)
        updateGameStatus(vm.getState())
    }
}