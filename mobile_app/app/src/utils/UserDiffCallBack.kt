package com.alice.rodexapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.alice.rodexapp.model.UserModel

class UserDiffCallBack(private val oldList: List<UserModel>, private val newList: List<UserModel>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].userid == newList[newItemPosition].userid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
