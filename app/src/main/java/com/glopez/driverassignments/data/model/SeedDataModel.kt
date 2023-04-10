package com.glopez.driverassignments.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Data Model used when parsing seed data from json file.
 * @param shipments list of shipment names
 * @param drivers list of driver names
 */
data class SeedDataModel(
    @SerializedName("shipments")
    @Expose
    val shipments: List<String>?,

    @SerializedName("drivers")
    @Expose
    val drivers: List<String>?,
)
