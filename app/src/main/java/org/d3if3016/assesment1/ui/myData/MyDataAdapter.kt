package org.d3if3016.assesment1.ui.myData

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3if3016.assesment1.R
import org.d3if3016.assesment1.data.Vehicles
import org.d3if3016.assesment1.databinding.ListDataBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyDataAdapter(private val context: Context, private val onShare: (String) -> Unit) : ListAdapter<Vehicles, MyDataAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Vehicles>() {
                override fun areItemsTheSame(oldItem: Vehicles, newItem: Vehicles): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Vehicles, newItem: Vehicles): Boolean {
                    return oldItem == newItem
                }
            }
    }

    inner class ViewHolder(
        private val binding: ListDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("en", "ID"))

        fun bind(vehicles: Vehicles) = with(binding) {
            val dataDate = dateFormatter.format(Date(vehicles.date))
            vehiclesTextView.text = context.getString(
                R.string.vehicles_data,
                vehicles.bike,
                vehicles.motorcycle,
                vehicles.car,
                vehicles.miniTruck,
                vehicles.bus,
                vehicles.truck
            )
            timeTextView.text = vehicles.time
            dateTextView.text = dataDate
            val message = context.getString(
                R.string.vehicles_share_data,
                dataDate,
                vehicles.bike,
                vehicles.motorcycle,
                vehicles.car,
                vehicles.miniTruck,
                vehicles.bus,
                vehicles.truck,
                vehicles.time
            )
            shareButton.setOnClickListener {
                onShare(message)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListDataBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}