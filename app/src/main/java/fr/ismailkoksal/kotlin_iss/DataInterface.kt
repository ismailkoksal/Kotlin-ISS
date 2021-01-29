package fr.ismailkoksal.kotlin_iss

import android.content.Context
import fr.ismailkoksal.kotlin_iss.entities.Astro

class DataInterface {
    companion object {
        fun addItem(context: Context, newItem: Astro) {
            AppDatabase(context).astroDao().insertAll(newItem)
        }

        fun getAll(context: Context): List<Astro> {
            return AppDatabase(context).astroDao().getAll()
        }

        fun deleteAll(context: Context) {
            AppDatabase(context).astroDao().deleteAll()
        }
    }
}