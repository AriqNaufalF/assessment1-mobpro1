package org.d3if3016.assesment1.ui.counter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3if3016.assesment1.data.Vehicle
import org.d3if3016.assesment1.databinding.ListItemBinding

class CounterAdapter(private val onClick: (Vehicle) -> Unit) : ListAdapter<Vehicle, CounterAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
    private val DIFF_CALLBACK =
        object : DiffUtil.ItemCallback<Vehicle>() {
            override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem == newItem
            }
        }
}

    inner class ViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicle: Vehicle) = with(binding) {
            vehicleName.text = vehicle.name
            vehicleImage.setImageResource(vehicle.imgRes)
            vehicleCounter.text = vehicle.count.toString()
            root.setOnClickListener {
                onClick(vehicle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}