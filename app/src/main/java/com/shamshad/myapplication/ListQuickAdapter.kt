package com.shamshad.myapplication


import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.shamshad.myapplication.data.model.ListData
import com.shamshad.myapplication.databinding.ListItemBinding

class ListQuickAdapter(data: MutableList<ListData>?): BaseQuickAdapter<ListData, BaseDataBindingHolder<ListItemBinding>>(
    R.layout.list_item,data
) {

    override fun convert(holder: BaseDataBindingHolder<ListItemBinding>, item: ListData) {
        holder.dataBinding?.apply {
            list = item
        }
    }
    class DiffCallback: DiffUtil.ItemCallback<ListData>() {
        override fun areItemsTheSame(oldItem: ListData, newItem: ListData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListData, newItem: ListData): Boolean {
            return (oldItem.id == newItem.id &&
                    oldItem.title == newItem.title)
        }
    }

}