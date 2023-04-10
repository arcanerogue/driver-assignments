package com.glopez.driverassignments.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glopez.driverassignments.data.model.DriverAssignmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverAssignmentDao {
    @Query("SELECT * FROM driverAssignments")
    fun getDriverAssignments(): Flow<List<DriverAssignmentEntity>>

    @Query("SELECT * FROM driverAssignments WHERE id = :id")
    suspend fun getDriverAssignmentById(id: Int): DriverAssignmentEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriverAssignment(assignment: DriverAssignmentEntity)

    @Query("SELECT COUNT(id) FROM driverAssignments")
    fun getCount(): Int
}