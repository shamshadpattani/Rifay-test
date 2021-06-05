package com.shamshad.myapplication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shamshad.myapplication.databinding.ActivityAddPostBinding

class AddPostFragment(): BottomSheetDialogFragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: ActivityAddPostBinding


    companion object {
        fun newInstances(): AddPostFragment {
            val f = AddPostFragment()
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
       // viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.activity_add_post,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observer()
        //setAdjustScreen();
    }


    private fun observer() {
        viewModel.dismissDialog.observe(viewLifecycleOwner) { dismiss ->
            if(dismiss) {
                dismiss()
            }
        }
        viewModel.dataLoading.observe(this){
            if(it){
                binding.saveBtn.visibility=View.GONE;
            }else{
                binding.saveBtn.visibility=View.VISIBLE;
            }
        }

    }

    private fun init() {

        binding.saveBtn.setOnClickListener {
            when {
                viewModel.title.value=="" -> {
                    binding.inputTitle.error="must be fill i it"
                }
                viewModel.descrp.value=="" -> {
                    binding.inputDescrp.error="must be fill it"
                }
                else -> {
                    viewModel.savePost()
                    binding.saveBtn.visibility=View.INVISIBLE;
                    Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
                }
            }

        }
        binding.cancelBtn.setOnClickListener {
            viewModel.discardChanges()
        }
    }


}