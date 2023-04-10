package com.glopez.driverassignments.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driverAssignments")
data class DriverAssignmentEntity(
    val driverName: String,
    val shipmentName: String,

    @PrimaryKey
    val id: Int
)
