package fr.ismailkoksal.kotlin_iss

import android.content.Context
import fr.ismailkoksal.kotlin_iss.entities.Astro
import fr.ismailkoksal.kotlin_iss.dao.AstroDao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Astro::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun astroDao(): AstroDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "iss.db"
        )
            .build()
    }
}