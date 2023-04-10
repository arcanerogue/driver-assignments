package com.glopez.driverassignments.domain.repository

import com.glopez.driverassignments.data.model.SeedDataModel

interface SeedDataSource {
    fun parseSeedData(): SeedDataModel?
}