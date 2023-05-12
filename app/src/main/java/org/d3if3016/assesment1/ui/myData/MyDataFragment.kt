package org.d3if3016.assesment1.ui.myData

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.d3if3016.assesment1.R
import org.d3if3016.assesment1.data.VehiclesDb
import org.d3if3016.assesment1.databinding.FragmentMyDataBinding

class MyDataFragment : Fragment() {
    private val viewModel: MyDataViewModel by lazy {
        val db = VehiclesDb.getInstance(requireContext())
        val factory = MyDataViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[MyDataViewModel::class.java]
    }

    private lateinit var binding: FragmentMyDataBinding
    private lateinit var myAdapter: MyDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyDataBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myAdapter = MyDataAdapter(requireContext()) { shareData(it) }
        with(binding.myDataRecyclerView) {
            adapter = myAdapter
            setHasFixedSize(true)
        }

        viewModel.data.observe(viewLifecycleOwner) {
            binding.emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            myAdapter.submitList(it)
        }

        /**
         * Fungsi untuk membuat method item touch helper untuk menambahkan fungsi geser untuk hapus
         * Disini juga menentukan arah geser dan posisi ke kiri
         */
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = viewModel.data.value?.get(viewHolder.adapterPosition)!!
                viewModel.deleteItem(deletedItem)
                myAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                Snackbar.make(
                    binding.myDataRecyclerView,
                    R.string.delete_item_message,
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.undo) {
                    viewModel.undoDeletedItem(deletedItem)
                }.show()
            }
        }).attachToRecyclerView(binding.myDataRecyclerView)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.my_data_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_menu) {
            deleteData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteData() {
        val deletedData = viewModel.data.value!!

        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.delete_all_confirmation)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteData()
                Snackbar.make(
                    binding.myDataRecyclerView,
                    R.string.delete_item_message,
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.undo) {
                    viewModel.undoDeletedData(deletedData)
                }.show()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun shareData(message: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(
                requireActivity().packageManager
            ) != null
        ) {
            startActivity(shareIntent)
        }
    }
}