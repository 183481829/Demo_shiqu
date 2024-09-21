package com.xyl.demo01.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.xyl.demo01.R
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
        //返回按钮
        binding.backButton.setOnClickListener { backUp() }
        // 点击头像
        binding.avatarImageView.setOnClickListener { openImageSelector() }

        //选择性别
        binding.boyRadioButton.setOnClickListener { showGender("boy") }
        binding.girlRadioButton.setOnClickListener { showGender("girl") }

        // 选择生日
        binding.birthdayTextView.setOnClickListener { showDatePicker() }

        // 完成按钮
        binding.completeButton.setOnClickListener {
            Toast.makeText(this, "资料已保存", Toast.LENGTH_SHORT).show()
        }

    }

    private fun backUp() {
        println("返回上一级")
    }

    // 拍照或选择图片
    private fun openImageSelector(){
        val options = arrayOf("拍照", "从相册选择")
        val builder = AlertDialog.Builder(this)
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> takePhoto()
                1 -> selectImageFromGallery()
            }
        }
        builder.show()
        
    }

    // 启动相机
    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap!=null){
            binding.avatarImageView.setImageBitmap(bitmap)
        }
//        bitmap?.let {
//            binding.avatarImageView.setImageBitmap(it)
//        }
    }

    private fun takePhoto() {
        takePhotoLauncher.launch(null)
    }

    // 从相册选择图片
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri:    Uri? ->
        uri?.let {
            binding.avatarImageView.setImageURI(it)
        }
    }

    private fun selectImageFromGallery() {
        selectImageLauncher.launch("image/*")
    }
    private fun showGender(gender: String) {
        when(gender){
            "boy"-> println("选择的是男孩")
            "girl"-> println("选择的是女孩")
        }

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
