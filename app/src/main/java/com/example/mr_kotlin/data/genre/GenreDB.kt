package com.example.mr_kotlin.data.genre

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GenreLocalDB")
data class GenreDB(

    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "PiechartMap") var PiechartMap: MutableMap<String,Double>)
