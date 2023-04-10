package com.glopez.driverassignments.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glopez.driverassignments.databinding.DriverItemBinding
import com.glopez.driverassignments.domain.model.DriverAssignment


class DriversAdapter(private val listener: OnItemClickListener) :
    ListAdapter<DriverAssignment, DriversAdapter.DriverViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val binding = DriverItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DriverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class DriverViewHolder(private val binding: DriverItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val driverAssignment = getItem(position)
                        listener.onDriverClick(driverAssignment, shipmentName)
                    }
                }
            }
        }

        fun bind(driverAssignment: DriverAssignment) {
            binding.apply {
                driverName.text = driverAssignment.driverName
            }
        }
    }

    interface OnItemClickListener {
        fun onDriverClick(driverAssignment: DriverAssignment, view: TextView)
    }

    class DiffCallback : DiffUtil.ItemCallback<DriverAssignment>() {
        override fun areItemsTheSame(oldItem: DriverAssignment, newItem: DriverAssignment) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: DriverAssignment, newItem: DriverAssignment) =
            oldItem == newItem

    }
}