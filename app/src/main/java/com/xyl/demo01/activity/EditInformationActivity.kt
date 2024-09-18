package com.xyl.demo01

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.xyl.demo01.databinding.ActivityEditIinformationBinding
import java.util.Calendar

class EditInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditIinformationBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_iinformation)
        binding = ActivityEditIinformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 点击头像
        binding.avatarImageView.setOnClickListener { openImageSelector() }

        // 选择生日
        binding.birthdayTextView.setOnClickListener { showDatePicker() }

        // 完成按钮
        binding.completeButton.setOnClickListener {
            Toast.makeText(this, "资料已保存", Toast.LENGTH_SHORT).show()
        }

    }
    private fun openImageSelector(){
        println("打开相册选择图片")
    }
    private fun showDatePicker(){
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            binding.birthdayTextView.text = date
        }, year, month, day).show()
    }

}