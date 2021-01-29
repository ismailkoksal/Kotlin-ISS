package fr.ismailkoksal.kotlin_iss.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Astro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "craft") val craft: String,
    @ColumnInfo(name = "name") val name: String,
)