package com.shamshad.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.shamshad.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: ListQuickAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
        initAdapter()
        observer()
        init()
    }

    private fun init() {
        binding.createListFab.setOnClickListener {
            showAddDialog()
            mainViewModel.activeDialog()
        }
    }
    private fun showAddDialog() {
        val dialog = AddPostFragment.newInstances()
        dialog.isCancelable = false
        dialog.show(this.supportFragmentManager, "TAG")
    }
    private fun initAdapter() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.setHasFixedSize(false)
        mAdapter = ListQuickAdapter(mutableListOf())
        binding.recyclerview.adapter = mAdapter
        mAdapter.setDiffCallback(ListQuickAdapter.DiffCallback())
    }

    private fun observer() {
        mainViewModel.listData.observe(this){ it->
            binding.pBar.visibility= View.GONE
            mAdapter.setDiffNewData(it.toMutableList())
        }
        mainViewModel.dataLoading.observe(this){
            binding.pBar.visibility= View.VISIBLE
        }

    }
}