package io.github.tuguzt.pcbuilder.presentation.view.diffutils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import io.github.tuguzt.pcbuilder.domain.model.Identifiable

class DiffCallback<I : Comparable<I>, T : Identifiable<I>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}
