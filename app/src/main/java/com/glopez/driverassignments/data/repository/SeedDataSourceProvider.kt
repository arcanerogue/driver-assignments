package com.glopez.driverassignments.data.repository

import android.content.Context
import android.util.Log
import com.glopez.driverassignments.R
import com.glopez.driverassignments.data.model.SeedDataModel
import com.glopez.driverassignments.domain.repository.SeedDataSource
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.IOException
import java.text.ParseException
import javax.inject.Inject

class SeedDataSourceProvider @Inject constructor(private val context: Context) : SeedDataSource {
    override fun parseSeedData(): SeedDataModel? {
        val gson = GsonBuilder().create()
        try {
            val inputStream = context.resources.openRawResource(R.raw.seed_data)
            val reader = BufferedReader(inputStream.reader())
            return gson.fromJson(reader, SeedDataModel::class.java)
        } catch (ex: ParseException) {
            Log.e(this.javaClass.name, "Unable to parse data from seed_data.json")
        } catch (ex: IOException) {
            Log.e(this.javaClass.name, "Error while processing seed data")
        }
        return null
    }
}