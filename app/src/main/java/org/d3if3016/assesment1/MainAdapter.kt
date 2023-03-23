package org.d3if3016.assesment1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import org.d3if3016.assesment1.databinding.ListItemBinding

class MainAdapter(private val data: List<Vehicle>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicle: Vehicle) = with(binding) {
            vehicleName.text = vehicle.name
            vehicleImage.setImageResource(vehicle.imgRes)

            root.setOnClickListener {
                vehicle.count += 1
                vehicleCounter.text = vehicle.count.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}