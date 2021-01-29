package fr.ismailkoksal.kotlin_iss.dao

import fr.ismailkoksal.kotlin_iss.entities.Astro
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AstroDao {
    @Query("SELECT * FROM astro")
    fun getAll(): List<Astro>

    @Query("SELECT * FROM astro WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Astro>

    @Query("SELECT * FROM astro WHERE name LIKE :first LIMIT 1")
    fun findByName(first: String): Astro

    @Insert
    fun insertAll(vararg astro: Astro)

    @Delete
    fun delete(astro: Astro)

    @Query("DELETE FROM astro")
    fun deleteAll()
}