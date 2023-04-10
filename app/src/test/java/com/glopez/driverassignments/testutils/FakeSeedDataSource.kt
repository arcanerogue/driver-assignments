package com.glopez.driverassignments.testutils

import com.glopez.driverassignments.data.model.SeedDataModel
import com.glopez.driverassignments.domain.repository.SeedDataSource

class FakeSeedDataSource(
    private val drivers: List<String>?,
    private val shipments: List<String>?
    ) : SeedDataSource {

    override fun parseSeedData(): SeedDataModel {
        return SeedDataModel(shipments, drivers)
    }
}