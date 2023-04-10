package com.glopez.driverassignments.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.glopez.driverassignments.data.model.DriverAssignmentEntity

@Database(
    entities = [DriverAssignmentEntity::class],
    version = 1,
    exportSchema = false)
abstract class DriverAssignmentDatabase : RoomDatabase() {
    abstract fun driverAssignmentDao(): DriverAssignmentDao

    companion object {
        const val DATABASE_NAME = "driver_assignment_database"
    }
}