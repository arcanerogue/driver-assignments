package com.glopez.driverassignments.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.glopez.driverassignments.R
import com.glopez.driverassignments.databinding.ActivityMainBinding
import com.glopez.driverassignments.domain.model.DriverAssignment
import com.glopez.driverassignments.utils.setGone
import com.glopez.driverassignments.utils.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DriversAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DriversViewModel by viewModels()
    private var driversAdapter: DriversAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        driversAdapter = DriversAdapter(this)

        binding.apply {
            driversRecyclerView.apply {
                adapter = driversAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
        observeDriverAssignments()
    }

    private fun observeDriverAssignments() {
        viewModel.driverAssignments.observe(this) { uiState ->
            uiState.let {
                when (uiState) {
                    is UiState.Success -> {
                        hideLoading()
                        driversAdapter?.submitList(uiState.data)
                    }
                    is UiState.Loading -> {
                        showLoading()
                    }
                    is UiState.Error -> {
                        hideLoading()
                        showErrorDialog(uiState.message)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.driversProgressBar.setVisible()
    }

    private fun hideLoading() {
        binding.driversProgressBar.setGone()
    }

    private fun showErrorDialog(message: String?) {
        val dialogBuilder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }
        dialogBuilder.apply {
            setTitle(getString(R.string.dialog_destination_title))
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton(
                getString(R.string.dialog_destination_ok)
            ) { _, _ -> }
        }.show()
    }

    override fun onDriverClick(driverAssignment: DriverAssignment, view: TextView) {
        val destination = driverAssignment.shipmentName
        val message = destination.ifEmpty {
            getString(R.string.dialog_default_message)
        }
        view.text = message

        when {
            view.isGone -> view.setVisible()
            view.isVisible -> view.setGone()
            else -> {}
        }
    }
}