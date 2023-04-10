package com.glopez.driverassignments.di

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.glopez.driverassignments.data.db.*
import com.glopez.driverassignments.data.repository.SeedDataSourceProvider
import com.glopez.driverassignments.data.repository.DriverAssignmentsRepository
import com.glopez.driverassignments.domain.repository.SeedDataSource
import com.glopez.driverassignments.domain.repository.ShipmentsRepository
import com.glopez.driverassignments.utils.DefaultDispatcherProvider
import com.glopez.driverassignments.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideDriverAssignmentDatabase(@ApplicationContext context: Context): DriverAssignmentDatabase {
        return Room.databaseBuilder(
            context,
            DriverAssignmentDatabase::class.java,
            DriverAssignmentDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideShipmentsRepository(
        @ApplicationContext context: Context,
        database: DriverAssignmentDatabase,
        seedDataSource: SeedDataSource,
        dispatcher: DispatcherProvider
    ): ShipmentsRepository {
        return DriverAssignmentsRepository(
            seedDataSource,
            database.driverAssignmentDao(),
            ProcessLifecycleOwner.get().lifecycleScope,
            dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideSeedDataSource(@ApplicationContext context: Context): SeedDataSource {
        return SeedDataSourceProvider(context)
    }
}