package com.shamshad.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.shamshad.myapplication.databinding.ActivityMainBinding
import com.shamshad.myapplication.utlis.NetworkUtils

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: ListQuickAdapter
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
        initAdapter()
        observer()
        init()
        handleNetworkChanges()
    }

    private fun init() {
        binding.createListFab.setOnClickListener {
            showAddDialog()
            //  mainViewModel.activeDialog()
        }
    }

    private fun showAddDialog() {
        dialog = this?.let { Dialog(it, R.style.Theme_Design_BottomSheetDialog) }

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_add_post)
        dialog?.setCancelable(true)



        dialog?.findViewById<Button>(R.id.cancel_btn)?.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.findViewById<Button>(R.id.save_btn)?.setOnClickListener {
            var title =
                dialog?.findViewById<TextInputLayout>(R.id.inputTitle)?.editText?.text.toString()
            var desc =
                dialog?.findViewById<TextInputLayout>(R.id.inputDescrp)?.editText?.text.toString()
            when {
                title.isEmpty() -> {
                    dialog?.findViewById<TextInputLayout>(R.id.inputTitle)?.error =
                        "must be fill i it"
                }
                desc.isEmpty() -> {
                    dialog?.findViewById<TextInputLayout>(R.id.inputDescrp)?.error =
                        "must be fill it"
                }
                else -> {
                    dialog?.dismiss()
                    mainViewModel.savePost(title, desc)
                }
            }
        }

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog?.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        dialog?.show()
        dialog?.window?.attributes = lp
    }

    private fun initAdapter() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.setHasFixedSize(false)
        mAdapter = ListQuickAdapter(mutableListOf())
        binding.recyclerview.adapter = mAdapter
        mAdapter.setDiffCallback(ListQuickAdapter.DiffCallback())
    }

    private fun observer() {
        mainViewModel.getList(this)
        mainViewModel.listData.observe(this) { it ->

            mAdapter.setDiffNewData(it.toMutableList())
        }
        mainViewModel.dataLoading.observe(this) {
            if (it) {
                binding.pBar.visibility = View.VISIBLE
            } else {
                binding.pBar.visibility = View.GONE
            }

        }
        mainViewModel.posterror.observe(this){
            Toast.makeText(this, "Failed to Upload data", Toast.LENGTH_SHORT).show()
        }
        mainViewModel.ispostAdd.observe(this){
            if(it)
              Toast.makeText(this, " Upload data Suceesfully", Toast.LENGTH_SHORT).show()
        }
    }


    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->
            if (!isConnected) {
                binding.netView.visibility = View.VISIBLE
                binding.netStatus.text = getString(R.string.no_net)
                binding.netView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.colorDangerDark
                    )
                )
            } else {
                observer()
                binding.netStatus.text = getString(R.string.back_net)
                binding.netView.apply {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.colorSuccessDark
                        )
                    )
                    animate()
                        .alpha(1f)
                        .setStartDelay(1000.toLong())
                        .setDuration(1000)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                binding.netView.visibility = View.GONE
                            }
                        })
                }

            }
        })
    }
}